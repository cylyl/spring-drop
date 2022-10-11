package com.github.cylyl.springdrop;

import com.codahale.metrics.health.HealthCheck;
import com.github.cylyl.springdrop.example.config.ExampleConfiguration;
import com.github.cylyl.springdrop.example.controller.ExampleRESTController;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import javax.servlet.ServletRegistration;

@Order(Ordered.HIGHEST_PRECEDENCE)
public class ExampleApp extends Application<ExampleConfiguration> {
    public ExampleApp() {
    }

    @Override
    public void initialize(Bootstrap<ExampleConfiguration> b) {
    }

    @Override
    public void run(ExampleConfiguration c, Environment e) {

        ServletRegistration.Dynamic servlet = new SpringDrop().createDispatcher(c, e,
                "com.github.cylyl.springdrop.example.config",
                "com.github.cylyl.springdrop.example");

        servlet.addMapping("/h2/*");
        e.jersey().register(servlet);

        e.jersey().register(new ExampleRESTController(e.getValidator()));
        e.jersey().register(new HealthCheck(){
            @Override
            protected Result check() throws Exception {
                return Result.healthy();
            }
        });
    }

    public static void main(String[] args) throws Exception {
        new ExampleApp().run(args);
    }
}

