package com.tacoloco.webservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * Application for managing order items.
 */
@SpringBootApplication
@EnableCaching
public class OrderItemApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderItemApplication.class, args);
    }

}
