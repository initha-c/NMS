package com.hcl.routersimulator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.hcl.routersimulator.config.RouterSimulatorProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties(RouterSimulatorProperties.class)
public class RouterSimulatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(RouterSimulatorApplication.class, args);
    }
}
