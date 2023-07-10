package com.polarbookshop.dispatcherservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DispatcherServiceApplication {

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(DispatcherServiceApplication.class);
        springApplication.addListeners(new PropertiesLogger());
        springApplication.run(args);
    }

}
