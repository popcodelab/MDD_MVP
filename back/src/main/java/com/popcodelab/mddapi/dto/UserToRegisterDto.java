package com.popcodelab.mddapi.dto;

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
    @NotBlank
    @Size(min = 6)
    private String username;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Size(min = 8)
    private String password;
}
