package com.github.cylyl.springdrop.example.controller;

import javax.validation.Validator;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/hello")
@Produces(MediaType.APPLICATION_JSON)
public class ExampleRESTController {
    private final Validator validator;

    public ExampleRESTController(Validator validator) {
        this.validator = validator;
    }

    @GET
    public Response getExamples() {
        return Response.ok("hello").build();
    }
}
