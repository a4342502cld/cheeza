package com.layton.cheeza.demo.provider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ImportResource(locations = "classpath:spring/cheeza-server.xml")
public class CheezaDemoProviderApplication {
    public static void main(String[] args) {
        SpringApplication.run(CheezaDemoProviderApplication.class, args);
    }
}
