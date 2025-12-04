package com.example.rest;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/hello") // URL: /api/hello
public class HelloResourse {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response sayHello() {
        return Response.ok("{\"message\": \"Hello from Jakarta EE!\"}").build();
    }
}