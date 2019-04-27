package top.liuliyong;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableFeignClients(basePackages = "top.liuliyong.account.client")
public class PublicServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(PublicServerApplication.class, args);
    }

}
