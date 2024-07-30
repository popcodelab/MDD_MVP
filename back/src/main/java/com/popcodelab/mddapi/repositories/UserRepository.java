package com.popcodelab.mddapi.repositories;

import com.popcodelab.mddapi.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.Optional;

/**
 * The UserRepository interface represents a repository for User entities in the system.
 * It extends the JpaRepository interface, providing standard CRUD (Create, Read, Update, Delete) operations for User entities.
 * <p>
 * This interface provides methods for finding User entities by their ID, email, and username.
 * The methods return instances of User or Optional<User> depending on the expected result.
 * If the entity is not found, the methods may return null or an empty Optional, depending on the method.
 *
 * @author Pignon Pierre-Olivier
 * @version 1.0
 * @see JpaRepository
 * @see User
 * @see Optional
 */
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * Retrieves a User entity by its ID.
     *
     * @param id the ID of the User entity to retrieve
     * @return an Optional containing the User entity if found, or an empty Optional if not found
     */
    @NonNull
    Optional<User> findById(@NonNull Long id);

    /**
     * Retrieves a User entity by its email.
     *
     * @param email the email of the User entity to retrieve
     * @return the User entity
     */
    @NonNull
    User findByEmail(@NonNull String email);

    /**
     * Finds a user by their username.
     *
     * @param username the username of the user to find
     * @return the user with the specified username
     */
    @NonNull
    User findByUsername(@NonNull String username);
}
