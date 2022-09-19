package com.github.cylyl.springdrop.example.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

@Configuration
@EnableWebMvc
public class SpringWebConfig implements WebMvcConfigurer {
    @Bean
    public LocaleResolver localeResolver(){
        final AcceptHeaderLocaleResolver resolver = new AcceptHeaderLocaleResolver();
        return resolver;
    }
}
