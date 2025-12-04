package com.example.rest;

import com.example.dto.AuthRequest;
import com.example.dto.AuthResponse;
import com.example.models.User;
import com.example.security.JwtUtil;
import com.example.service.AuthService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Optional;

@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthResource {

    @Inject
    private AuthService authService;

    @Inject
    private JwtUtil jwtUtil;

    @POST
    @Path("/login")
    public Response login(AuthRequest authRequest) {
        Optional<User> userOptional = authService.authenticate(
                authRequest.getUsername(),
                authRequest.getPassword()
        );

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            String token = jwtUtil.generateToken(user);

            AuthResponse response = new AuthResponse(
                    token,
                    user.getName()
            );

            return Response.ok(response).build();
        }

        return Response.status(Response.Status.UNAUTHORIZED)
                .entity("Invalid username or password")
                .build();
    }

    @POST
    @Path("/register")
    public Response register(AuthRequest authRequest) {
        try {
            User newUser = new User();
            newUser.setName(authRequest.getUsername());
            newUser.setPassword(authRequest.getPassword()); // В реальном приложении хэшируйте пароль!

            User savedUser = authService.register(newUser);
            String token = jwtUtil.generateToken(savedUser);

            AuthResponse response = new AuthResponse(
                    token,
                    savedUser.getName()
            );

            return Response.ok(response).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        }
    }
}