package com.popcodelab.mddapi.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.ArrayList;
import java.util.List;

/**
 * The User class represents a user entity in the system.
 *
 * @author Pignon Pierre-Olivier
 * @version 1.0
 */
@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class User extends BaseEntity {


    /**
     * The userName variable represents the username of a user.
     */
    @Column(name = "user_name", nullable = false, unique = true, length = 64)
    private String username;

    /**
     * The email variable represents the email address of a user.
     */
    @Column(nullable = false, unique = true, length = 248)
    private String email;

    /**
     * The password variable represents the password field of a user entity in the system.
     */
    @Column(nullable = false, length = 64)
    private String password;

    /**
     * The subscribedTopicIds variable represents a list of topic IDs that a user is subscribed to.
     * It is annotated with @ElementCollection and FetchType.EAGER, indicating that it is a collection of elements
     * that should be eagerly fetched from the database.
     */
    @ElementCollection(fetch = FetchType.EAGER)
    private List<Long> subscribedTopicIds = new ArrayList<>();


}
