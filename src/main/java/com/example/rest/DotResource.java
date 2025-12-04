package com.example.rest;


import com.example.models.Dot;
import com.example.service.DotService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/dots")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DotResource {


    @Inject
    private DotService dotService;


    @GET
    @Path("/getAll")
    public Response getAllDots(){
        List<Dot> books = dotService.getAllDots();
        return Response.ok(books).build();
    }

    @POST
    @Path("/setDot")
    public void setDot(Dot dot) {
        dotService.createDot(dot);
    }
}
