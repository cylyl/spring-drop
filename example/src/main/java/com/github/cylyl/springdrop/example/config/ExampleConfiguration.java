package com.github.cylyl.springdrop.example.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;

import javax.validation.Valid;


public class ExampleConfiguration extends Configuration {

    @Valid
    @JsonProperty
    private SpringConfiguration spring;

    public SpringConfiguration getSpringConfiguration() {
        return spring;
    }
}
