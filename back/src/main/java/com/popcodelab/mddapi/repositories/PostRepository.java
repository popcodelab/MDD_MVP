package com.popcodelab.mddapi.repositories;

import com.popcodelab.mddapi.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * The PostRepository interface extends the JpaRepository interface,
 * providing CRUD operations for the Post entity type with a primary key of type Long.
 * <p>
 * This interface does not have any additional methods but inherits all the methods
 * defined in the JpaRepository interface for basic CRUD operations.
 * <p>
 * The Post entity represents a post in the system with properties such as title, content,
 * user ID, topic ID, and a list of comment IDs. It is an entity annotated with @Entity
 * and mapped to the "posts" table in the database. It also extends the BaseEntity class,
 * which provides common fields like id, createdAt, and updatedAt.
 * <p>
 * The BaseEntity class is an abstract class that serves as a base class for entities
 * in the system. It provides common fields such as id, createdAt, and updatedAt for all entities.
 * It is annotated with @MappedSuperclass, indicating that it is not a persistent entity itself
 * but can be used as a superclass for other entities.
 */
@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    //List<Post> findByTopicId(Long topicId);

    @Query("SELECT p FROM Post p WHERE p.topicId IN (:topicIds)")
    List<Post> findByTopicIds(@Param("topicIds") List<Long> topicIds);
}
