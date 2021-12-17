package org.example.evresponseserver;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@Slf4j
@SpringBootApplication
public class NettyServerApplication {
//    private static final String PROPERTIES =
//            "spring.config.location="
//                    + "file:config/application.properties";

    public static void main(String[] args) {
//        log.info("properties {}", PROPERTIES);

       new SpringApplicationBuilder(NettyServerApplication.class)
//                .properties(PROPERTIES)
                .run(args);
    }
}