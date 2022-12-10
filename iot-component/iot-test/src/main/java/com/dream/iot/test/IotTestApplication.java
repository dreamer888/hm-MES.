package com.dream.iot.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class IotTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(IotTestApplication.class);
    }

}
