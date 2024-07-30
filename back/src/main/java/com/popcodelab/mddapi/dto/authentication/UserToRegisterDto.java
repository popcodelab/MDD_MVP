package com.popcodelab.mddapi.dto.authentication;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The UserToRegisterDto class represents a data transfer object (DTO) that contains information
 * about a user to be registered. It is used for transferring user registration data between different
 * layers of the application.
 *
 * @author Pignon Pierre-Olivier
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserToRegisterDto {
    /**
     * The username field represents the username of a user to be registered.
     *
     * Constraints:
     * - The username must not be blank.
     * - The username must have a minimum length of 6 characters.
     */
    @NotBlank
    @Size(min = 6)
    private String username;
    /**
     * The email field represents the email address of a user to be registered.
     *
     * Constraints:
     * - The email must not be blank.
     * - The email must be a valid email address.
     */
    @NotBlank
    @Email
    private String email;
    /**
     * The password field represents the password of a user to be registered.
     *
     * Constraints:
     * - The password must not be blank.
     * - The password must have a minimum length of 8 characters.
     */
    @NotBlank
    @Size(min = 8)
    private String password;
}
