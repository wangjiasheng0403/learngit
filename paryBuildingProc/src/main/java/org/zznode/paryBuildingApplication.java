package org.zznode;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;


@SpringBootApplication
@EnableEurekaClient
public class paryBuildingApplication {
    public static void main(String[] args) {
        SpringApplication.run(paryBuildingApplication.class, args);
    }
}
