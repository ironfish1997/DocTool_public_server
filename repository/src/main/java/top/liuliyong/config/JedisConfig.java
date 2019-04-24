package top.liuliyong.config;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import top.liuliyong.util.JedisUtil;

/**
 * Jedis配置Bean
 *
 * @author liyong.liu 2019-03-14
 */
@Configuration
public class JedisConfig implements InitializingBean, DisposableBean {

    @Value("${redis.address}")
    private String redisAddress;

    @Override
    public void afterPropertiesSet() throws Exception {
        JedisUtil.init(redisAddress);
    }

    @Override
    public void destroy() throws Exception {
        JedisUtil.close();
    }

}
