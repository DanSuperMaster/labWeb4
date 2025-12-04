package com.example.rest;


import com.example.models.User;
import com.example.service.UserService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {


    @Inject
    private UserService userService;


    @GET
    @Path("/getAll")
    public Response getAllDots(){
        List<User> books = userService.getAllUsers();
        return Response.ok(books).build();
    }

    @POST
    @Path("/setUser")
    public void setDot(User user) {
        userService.createUser(user);
    }


}
