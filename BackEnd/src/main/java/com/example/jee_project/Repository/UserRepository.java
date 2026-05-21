package com.example.jee_project.Repository;

import com.example.jee_project.Entity.User;
import com.example.jee_project.Security.PasswordService;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import java.util.Optional;

/**
 * Repository for managing User persistence and database operations.
 */
@Stateless
public class UserRepository {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private PasswordService passwordService;

    /**
     * Persists a new user to the database after hashing their password.
     * @param username The unique username.
     * @param plaintextPassword The password in clear text.
     * @param role The role assigned to the user (e.g., ADMIN).
     * @return The persisted User entity.
     */
    public User createUser(String username, String plaintextPassword, String role) {
        String hashedPassword = passwordService.hashPassword(plaintextPassword);
        User newUser = new User(username, hashedPassword, role);
        em.persist(newUser);
        return newUser;
    }

    /**
     * Finds a user by their username using a JPQL query.
     * @param username The username to search for.
     * @return An Optional containing the User if found, or empty.
     */
    public Optional<User> findByUsername(String username) {
        try {
            User user = em.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class)
                    .setParameter("username", username)
                    .getSingleResult();
            return Optional.of(user);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    /**
     * Validates a user's credentials by comparing the input against the stored hash.
     * @param username The username provided at login.
     * @param plaintextPassword The password provided at login.
     * @return true if authentication succeeds, false otherwise.
     */
    public boolean authenticate(String username, String plaintextPassword) {
        return findByUsername(username)
                .map(user -> passwordService.verifyPassword(plaintextPassword, user.getPassword()))
                .orElse(false);
    }
}
