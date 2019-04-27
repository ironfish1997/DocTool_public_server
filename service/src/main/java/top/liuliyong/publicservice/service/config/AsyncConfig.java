package top.liuliyong.publicservice.service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * @Author liyong.liu
 * @Date 2019-04-24
 **/
@Configuration
@EnableAsync
public class AsyncConfig {
    /*
   此处成员变量应该使用@Value从配置中读取
    */
    @Value("${schedule.threadpool.corePoolSize}")
    private int corePoolSize;
    @Value("${schedule.threadpool.maxPoolSize}")
    private int maxPoolSize;
    @Value("${schedule.threadpool.queueCapacity}")
    private int queueCapacity;

    @Bean
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.initialize();
        return executor;
    }
}
