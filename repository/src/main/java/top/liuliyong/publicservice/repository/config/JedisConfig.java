package top.liuliyong.publicservice.repository.config;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import top.liuliyong.publicservice.repository.util.JedisUtil;

import java.util.Collections;
import java.util.List;

/**
 * Jedis配置Bean
 *
 * @author liyong.liu 2019-03-14
 */
@Configuration
public class RepoConfig implements InitializingBean, DisposableBean {

    @Value("${redis.address}")
    private String redisAddress;

    @Override
    public void afterPropertiesSet() {
        JedisUtil.init(redisAddress);
    }

    @Override
    public void destroy() {
        JedisUtil.close();
    }

    @Bean
    public MongoDbFactory mongoDbFactory() {
        //mongodb地址，集群环境填多个
        List<ServerAddress> seeds = Collections.singletonList(new ServerAddress("localhost", 27017));
        //用户认证信息，参数为用户，数据库，密码
        //MongoCredential com.mongodb.MongoCredential.createCredential(String userName, String database, char[] password)
        MongoCredential mongoCredential = MongoCredential.createCredential("DocTool", "DocTool", "123456".toCharArray());
        List<MongoCredential> credentialsList = Collections.singletonList(mongoCredential);
        //连接池参数配置
        MongoClientOptions.Builder builder = new MongoClientOptions.Builder();
        // 每个主机的连接数
        int connPerHost = 20;
        builder.connectionsPerHost(connPerHost);
        // 线程队列数
        int threadCount = 20;
        builder.threadsAllowedToBlockForConnectionMultiplier(threadCount);
        // 最大等待连接的线程阻塞时间（单位：毫秒）
        int maxWaitTime = 1000;
        builder.maxWaitTime(maxWaitTime);
        // 连接超时的时间。0是默认和无限（单位：毫秒）
        int timeOut = 1000;
        builder.connectTimeout(timeOut);
        MongoClientOptions options = builder.build();
        MongoClient mongoClient = new MongoClient(seeds, credentialsList, options);
        //这里第二个参数也就是cxytiandi是用户认证的库名,在哪个库认证就表示登陆哪个库
        return new SimpleMongoDbFactory(mongoClient, "DocTool");
    }

}
