package com.github.cylyl.springdrop;

import javax.validation.Validator;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/hello")
@Produces(MediaType.APPLICATION_JSON)
public class HelloRESTController {
    private final Validator validator;

    public HelloRESTController(Validator validator) {
        this.validator = validator;
    }

    @GET
    public Response getExamples() {
        return Response.ok("hello").build();
    }
}
