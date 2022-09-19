package com.github.cylyl.springdrop;

import com.codahale.metrics.health.HealthCheck;
import com.github.cylyl.springdrop.example.controller.ExampleRESTController;
import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

@Order(Ordered.HIGHEST_PRECEDENCE)
public class ExampleApp extends Application<Configuration> {
    public ExampleApp() {
    }

    @Override
    public void initialize(Bootstrap<Configuration> b) {
    }

    @Override
    public void run(Configuration c, Environment e) {
        new SpringDrop().initial(c, e,
                "com.github.cylyl.springdrop.example.config",
                "com.github.cylyl.springdrop.example");
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