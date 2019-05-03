package top.liuliyong.publicservice.repository.config;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import top.liuliyong.publicservice.repository.util.JedisUtil;

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
    public void afterPropertiesSet() {
        JedisUtil.init(redisAddress);
    }

    @Override
    public void destroy() {
        JedisUtil.close();
    }

}
