package top.liuliyong.publicservice.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;


/**
 * Redis client base on jedis
 *
 * @author xuxueli 2015-7-10 18:34:07
 */
@Component
public class JedisUtil {
    private static Logger logger = LoggerFactory.getLogger(JedisUtil.class);

    /**
     * redis address, like "{ip}"、"{ip}:{port}"、"{redis/rediss}:/xxx:{password}@{ip}:{port:6379}/{db}"；Multiple "," separated
     */
    private static String address;

    public static void init(String address) {
        JedisUtil.address = address;

        getInstance();
    }

    // ------------------------ ShardedJedisPool ------------------------
    /**
     * 方式01: Redis单节点 + Jedis单例 : Redis单节点压力过重, Jedis单例存在并发瓶颈 》》不可用于线上
     * new Jedis("127.0.0.1", 6379).get("cache_key");
     * 方式02: Redis单节点 + JedisPool单节点连接池 》》 Redis单节点压力过重，负载和容灾比较差
     * new JedisPool(new JedisPoolConfig(), "127.0.0.1", 6379, 10000).getResource().get("cache_key");
     * 方式03: Redis分片(通过client端集群,一致性哈希方式实现) + Jedis多节点连接池 》》Redis集群,负载和容灾较好, ShardedJedisPool一致性哈希分片,读写均匀，动态扩充
     * new ShardedJedisPool(new JedisPoolConfig(), new LinkedList<JedisShardInfo>());
     * 方式03: Redis集群；
     * new JedisCluster(jedisClusterNodes);
     */

    private static ShardedJedisPool shardedJedisPool;
    private static ReentrantLock INSTANCE_INIT_LOCL = new ReentrantLock(false);

    /**
     * 获取ShardedJedis实例
     */
    private static ShardedJedis getInstance() {
        if (shardedJedisPool == null) {
            try {
                if (INSTANCE_INIT_LOCL.tryLock(2, TimeUnit.SECONDS)) {

                    try {

                        if (shardedJedisPool == null) {

                            // JedisPoolConfig
                            JedisPoolConfig config = new JedisPoolConfig();
                            config.setMaxTotal(200);
                            config.setMaxIdle(50);
                            config.setMinIdle(8);
                            config.setMaxWaitMillis(10000);         // 获取连接时的最大等待毫秒数(如果设置为阻塞时BlockWhenExhausted),如果超时就抛异常, 小于零:阻塞不确定的时间,  默认-1
                            config.setTestOnBorrow(true);           // 在获取连接的时候检查有效性, 默认false
                            config.setTestOnReturn(false);          // 调用returnObject方法时，是否进行有效检查
                            config.setTestWhileIdle(true);          // Idle时进行连接扫描
                            config.setTimeBetweenEvictionRunsMillis(30000);     // 表示idle object evitor两次扫描之间要sleep的毫秒数
                            config.setNumTestsPerEvictionRun(10);               // 表示idle object evitor每次扫描的最多的对象数
                            config.setMinEvictableIdleTimeMillis(60000);        // 表示一个对象至少停留在idle状态的最短时间，然后才能被idle object evitor扫描并驱逐；这一项只有在timeBetweenEvictionRunsMillis大于0时才有意义


                            // JedisShardInfo List
                            List<JedisShardInfo> jedisShardInfos = new LinkedList<>();

                            String[] addressArr = address.split(",");
                            for (String s : addressArr) {
                                JedisShardInfo jedisShardInfo = new JedisShardInfo(s);
                                jedisShardInfos.add(jedisShardInfo);
                            }
                            shardedJedisPool = new ShardedJedisPool(config, jedisShardInfos);
                            logger.info(">>>>>>>>>>> JedisUtil.ShardedJedisPool init success.");
                        }

                    } finally {
                        INSTANCE_INIT_LOCL.unlock();
                    }
                }

            } catch (InterruptedException e) {
                logger.error(e.getMessage(), e);
            }
        }

        if (shardedJedisPool == null) {
            throw new NullPointerException(">>>>>>>>>>> JedisUtil.ShardedJedisPool is null.");
        }

        return shardedJedisPool.getResource();
    }

    public static void close() {
        if (shardedJedisPool != null) {
            shardedJedisPool.close();
        }
    }


    // ------------------------ serialize and unserialize ------------------------

    /**
     * 将对象-->byte[] (由于jedis中不支持直接存储object所以转换成byte[]存入)
     */
    private static byte[] serialize(Object object) {
        ObjectOutputStream oos = null;
        ByteArrayOutputStream baos = null;
        try {
            // 序列化
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
            return baos.toByteArray();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            try {
                Objects.requireNonNull(oos).close();
                Objects.requireNonNull(baos).close();
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            }
        }
        return null;
    }

    /**
     * 将byte[] -->Object
     */
    private static Object unserialize(byte[] bytes) {
        ByteArrayInputStream bais = null;
        try {
            // 反序列化
            bais = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bais);
            return ois.readObject();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            try {
                Objects.requireNonNull(bais).close();
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            }
        }
        return null;
    }

    // ------------------------ jedis util ------------------------
    /*
      存储简单的字符串或者是Object 因为jedis没有封装直接存储Object的方法，所以在存储对象需斟酌下
      存储对象的字段是不是非常多而且是不是每个字段都用到，如果是的话那建议直接存储对象，
      否则建议用集合的方式存储，因为redis可以针对集合进行日常的操作很方便而且还可以节省空间
     */

    /**
     * Set String
     *
     * @param seconds 存活时间,单位/秒
     */
    public static String setStringValue(String key, String value, int seconds) {
        String result = null;
        try (ShardedJedis client = getInstance()) {
            result = client.setex(key, seconds, value);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return result;
    }

    /**
     * Set Object
     *
     * @param seconds 存活时间,单位/秒
     */
    public static String setObjectValue(String key, Object obj, int seconds) {
        String result = null;
        try (ShardedJedis client = getInstance()) {
            result = client.setex(key.getBytes(), seconds, serialize(obj));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return result;
    }

    /**
     * Push obj to the front of key list,if key list is not exist in Redis, create one and push
     *
     * @param listExpireSecond expire time of the key,
     *                         if less than 0, the key list will persist.
     *                         if equals 0, it will be ignored
     * @return length of key list after push
     */
    public static long lpush(String key, Object obj, int listExpireSecond) {
        long result;
        try (ShardedJedis client = getInstance()) {
            result = client.lpush(serialize(key), serialize(obj));
            if (listExpireSecond > 0) {
                client.expire(serialize(key), listExpireSecond);
            } else if (listExpireSecond < 0) {
                client.persist(serialize(key));
            }
        }
        return result;
    }

    /**
     * Push obj to the front of key list,if key list is not exist in Redis, create one and push
     *
     * @param listExpireDeadline expire deadline of the key, should be timestamp long value
     * @return length of key list after push
     */
    public static long lpush(String key, Object obj, long listExpireDeadline) {
        long result;
        try (ShardedJedis client = getInstance()) {
            result = client.lpush(serialize(key), serialize(obj));
            if (listExpireDeadline > 0) {
                client.expireAt(serialize(key), listExpireDeadline);
            } else if (listExpireDeadline < 0) {
                client.persist(serialize(key));
            }
        }
        return result;
    }

    /**
     * show the length of key list
     */
    public static long llen(String key) {
        long result;
        try (ShardedJedis client = getInstance()) {
            result = client.llen(serialize(key));
        }
        return result;
    }

    /**
     * remove value from key list
     */
    public static long lrem(String key, Object value) {
        long result;
        try (ShardedJedis client = getInstance()) {
            result = client.lrem(serialize(key), 0, serialize(value));
        }
        return result;
    }


    /**
     * get key list items in specific range
     */
    public static List<Object> lrange(String key, int start, int end) {
        List<byte[]> Bresult;
        try (ShardedJedis client = getInstance()) {
            Bresult = client.lrange(serialize(key), start, end);
        }
        List<Object> result = new ArrayList<>();
        if (Bresult.size() == 0) {
            return result;
        }
        for (byte[] resultItemB : Bresult) result.add(unserialize(resultItemB));
        return result;
    }

    /**
     * get all items from key list
     */
    public static List<Object> lgetAll(String key) {
        return JedisUtil.lrange(key, 0, -1);
    }


    /**
     * Get String
     */
    public static String getStringValue(String key) {
        String value = "";
        try (ShardedJedis client = getInstance()) {
            value = client.get(key);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return value;
    }

    /**
     * Get Object
     */
    public static Object getObjectValue(String key) {
        Object obj = null;
        try (ShardedJedis client = getInstance()) {
            byte[] bytes = client.get(key.getBytes());
            if (bytes != null && bytes.length > 0) {
                obj = unserialize(bytes);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return obj;
    }

    /**
     * Delete key
     *
     * @return Integer reply, specifically:
     * an integer greater than 0 if one or more keys were removed
     * 0 if none of the specified key existed
     */
    public static long del(String key) {
        Long result = 0L;
        try (ShardedJedis client = getInstance()) {
            result = client.del(key);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return result;
    }

    /**
     * Delete key
     *
     * @return Integer reply, specifically:
     * an integer greater than 0 if one or more keys were removed
     * 0 if none of the specified key existed
     */
    public static long delSerialized(String key) {
        Long result = 0L;
        try (ShardedJedis client = getInstance()) {
            result = client.del(serialize(key));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return result;
    }

    /**
     * incrBy i(+i)
     *
     * @return new value after incr
     */
    public static Long incrBy(String key, int i) {
        Long result = null;
        try (ShardedJedis client = getInstance()) {
            result = client.incrBy(key, i);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return result;
    }

    /**
     * exists valid
     *
     * @return Boolean reply, true if the key exists, otherwise false
     */
    public static boolean exists(String key) {
        Boolean result = false;
        try (ShardedJedis client = getInstance()) {
            result = client.exists(key);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return result;
    }

    /**
     * expire reset
     *
     * @param seconds 存活时间,单位/秒
     * @return Integer reply, specifically:
     * 1: the timeout was set.
     * 0: the timeout was not set since the key already has an associated timeout (versions lt 2.1.3), or the key does not exist.
     */
    public static long expire(String key, int seconds) {
        Long result = 0L;
        try (ShardedJedis client = getInstance()) {
            result = client.expire(key, seconds);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return result;
    }

    /**
     * expire at unixTime
     */
    public static long expireAt(String key, long unixTime) {
        Long result = 0L;
        try (ShardedJedis client = getInstance()) {
            result = client.expireAt(key, unixTime);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return result;
    }
}
