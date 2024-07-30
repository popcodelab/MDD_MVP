package com.popcodelab.mddapi.dto.authentication;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * The UserDto class represents a data transfer object (DTO) that contains information about a user.
 * It is used for transferring user data between different layers of the application.
 *
 * @author Pignon Pierre-Olivier
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    /**
     * Stores the unique identifier for this object.
     */
    private Long id;
    /**
     * Stores the name of a user.
     */
    private String username;

    /**
     * Stores the email address of a user.
     */
    private String email;

    /**
     * This private variable represents a list of topic IDs that a user is subscribed to.
     * The topic IDs are stored as Long values.
     */
    private List<Long> subscribedTopicIds;


}
