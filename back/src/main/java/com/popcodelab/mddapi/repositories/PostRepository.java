package com.popcodelab.mddapi.repositories;

import com.popcodelab.mddapi.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * The PostRepository interface extends the JpaRepository interface,
 * providing CRUD operations for the Post entity type with a primary key of type Long.
 *
 * This interface does not have any additional methods but inherits all the methods
 * defined in the JpaRepository interface for basic CRUD operations.
 *
 * The Post entity represents a post in the system with properties such as title, content,
 * user ID, topic ID, and a list of comment IDs. It is an entity annotated with @Entity
 * and mapped to the "posts" table in the database. It also extends the BaseEntity class,
 * which provides common fields like id, createdAt, and updatedAt.
 *
 * The BaseEntity class is an abstract class that serves as a base class for entities
 * in the system. It provides common fields such as id, createdAt, and updatedAt for all entities.
 * It is annotated with @MappedSuperclass, indicating that it is not a persistent entity itself
 * but can be used as a superclass for other entities.
 */
public interface PostRepository extends JpaRepository<Post, Long> {
}
