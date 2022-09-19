package com.github.cylyl.springdrop.example.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class SpringConfig {
    @Autowired
    private Environment environment;

    @Bean
    public Object doNothing() {
        return null;
    }
}
