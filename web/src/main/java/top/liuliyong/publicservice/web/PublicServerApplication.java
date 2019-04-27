package top.liuliyong.publicservice.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableFeignClients(basePackages = "top.liuliyong.account.client")
@ComponentScan(basePackages = {"top.liuliyong.publicservice.*"})
public class PublicServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(PublicServerApplication.class, args);
    }

}
