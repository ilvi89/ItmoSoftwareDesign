package ru.abolsoft.sseconnect;

import org.aspectj.lang.annotation.Aspect;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
@EnableAspectJAutoProxy
public class SseConnectApplication {

    public static void main(String[] args) {
        SpringApplication.run(SseConnectApplication.class, args);
    }

}
