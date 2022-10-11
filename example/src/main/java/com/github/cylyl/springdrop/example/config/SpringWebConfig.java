package com.github.cylyl.springdrop.example.config;


import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.*;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

@Configuration
@EnableWebMvc
@PropertySources({
        @PropertySource("classpath:application.properties"),
})
@EnableAutoConfiguration
@EnableJpaRepositories
@SpringBootApplication
@ComponentScan(basePackages = {"com.github.cylyl.springdrop.example"})
public class SpringWebConfig implements WebMvcConfigurer {
    @Bean
    public LocaleResolver localeResolver(){
        final AcceptHeaderLocaleResolver resolver = new AcceptHeaderLocaleResolver();
        return resolver;
    }


}
