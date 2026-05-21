package com.example.frontend.Client;

public class UserSession {
    private static UserSession instance;

    private String token;
    private String username;
    private String role;

    // Private constructor prevents direct instantiation from other classes
    private UserSession() {}

    public static synchronized UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    // Clear session details on logout
    public void cleanSession() {
        this.token = null;
        this.username = null;
        this.role = null;
    }
}