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
 * @version 2.0
 * @see JpaRepository
 * @see User
 * @see Optional
 */
public interface UserRepository extends JpaRepository<User, Long> {
    @NonNull
    Optional<User> findById(@NonNull Long id);

    @NonNull
    User findByEmail(@NonNull String email);

    @NonNull
    User findByUsername(@NonNull String username);
}
