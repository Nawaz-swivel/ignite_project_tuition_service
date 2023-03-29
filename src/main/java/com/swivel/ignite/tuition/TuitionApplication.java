package com.swivel.ignite.tuition;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * Tuition Application
 */
@SpringBootApplication
@EnableEurekaClient
public class TuitionApplication {

    public static void main(String[] args) {
        SpringApplication.run(TuitionApplication.class, args);
    }

}
