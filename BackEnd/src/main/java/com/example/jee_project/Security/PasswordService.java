package com.example.jee_project.Security;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.security.enterprise.identitystore.Pbkdf2PasswordHash;
import java.util.HashMap;
import java.util.Map;

/**
 * Service for handling password hashing and verification using PBKDF2.
 */
@ApplicationScoped
public class PasswordService {

    @Inject
    private Pbkdf2PasswordHash passwordHash;

    /**
     * Initializes the PBKDF2 algorithm configuration.
     */
    @PostConstruct
    public void init() {
        Map<String, String> config = new HashMap<>();
        config.put("Pbkdf2PasswordHash.Algorithm", "PBKDF2WithHmacSHA256");
        config.put("Pbkdf2PasswordHash.Iterations", "2048");
        config.put("Pbkdf2PasswordHash.SaltSizeBytes", "32");

        passwordHash.initialize(config);
    }

    /**
     * Generates a secure PBKDF2 hash for a plaintext password.
     * @param plaintextPassword The password to hash.
     * @return The hashed password string.
     */
    public String hashPassword(String plaintextPassword) {
        if (plaintextPassword == null) {
            throw new IllegalArgumentException("Password cannot be null");
        }
        return passwordHash.generate(plaintextPassword.toCharArray());
    }

    /**
     * Verifies if a plaintext password matches a stored PBKDF2 hash.
     * Supports a debug fallback for plain text passwords (no colon) during testing.
     * @param plaintextPassword The input password.
     * @param hashedPassword The stored hash or plaintext fallback.
     * @return true if the password matches, false otherwise.
     */
    public boolean verifyPassword(String plaintextPassword, String hashedPassword) {
        if (plaintextPassword == null || hashedPassword == null) {
            return false;
        }
        
        // General fallback: if stored "hash" has no colon, treat it as plain text
        if (!hashedPassword.contains(":")) {
            boolean matches = plaintextPassword.equals(hashedPassword);
            if (matches) {
                System.out.println("Debug: Plaintext password match for user");
            }
            return matches;
        }

        try {
            return passwordHash.verify(plaintextPassword.toCharArray(), hashedPassword);
        } catch (Exception e) {
            System.err.println("Error verifying password: " + e.getMessage());
            // Last resort fallback
            return plaintextPassword.equals(hashedPassword);
        }
    }
}
