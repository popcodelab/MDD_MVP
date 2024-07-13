package com.popcodelab.mddapi.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * BaseEntity is an abstract class that serves as a base class for entities in the system.
 * It provides common fields such as id, createdAt, and updatedAt for all entities.
 * <p>
 * This class is annotated with @MappedSuperclass, indicating that it is not a persistent entity itself
 * but can be used as a superclass for other entities.
 *
 * @author Pignon Pierre-Olivier
 * @version 1.0
 */
@MappedSuperclass
@Data
public abstract class BaseEntity {

    /**
     * The `id` variable is a unique identifier for an entity in the system.
     * It is annotated with @Id, indicating that it represents the primary key of the entity.
     */
    @Id
    @Column(unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The `createAt` variable represents the date and time when the entity was created.
     */
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    /**
     * The `updatedAt` variable represents the date and time when the entity was last updated.
     */
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
