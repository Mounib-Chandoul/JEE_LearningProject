package com.example.jee_project;

import com.example.jee_project.Entity.User;
import com.example.jee_project.Repository.UserRepository;
import com.example.jee_project.Security.PasswordService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.Map;

/**
 * REST Resource for handling authentication-related requests.
 */
@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthResource {

    @Inject
    private UserRepository userRepository;

    @Inject
    private PasswordService passwordService;

    /**
     * Handles user login requests by verifying credentials and issuing a session token.
     * @param loginRequest The User object containing username and password from the client.
     * @return A Response object with status 200 (OK) and session data, or 401 (Unauthorized).
     */
    @POST
    @Path("/login")
    public Response login(User loginRequest) {
        System.out.println("Login attempt for: " + loginRequest.getUsername());
        
        return userRepository.findByUsername(loginRequest.getUsername())
                .map(user -> {
                    boolean matches = passwordService.verifyPassword(loginRequest.getPassword(), user.getPassword());
                    System.out.println("Password match: " + matches);
                    
                    if (matches) {
                        return Response.ok(Map.of(
                                "token", "secure-session-token-xyz-123",
                                "username", user.getUsername(),
                                "role", user.getRole() != null ? user.getRole() : "USER"
                        )).build();
                    } else {
                        return Response.status(Response.Status.UNAUTHORIZED)
                                .entity(Map.of("error", "Invalid password"))
                                .build();
                    }
                })
                .orElseGet(() -> {
                    System.out.println("User not found: " + loginRequest.getUsername());
                    return Response.status(Response.Status.UNAUTHORIZED)
                            .entity(Map.of("error", "User not found"))
                            .build();
                });
    }
}
