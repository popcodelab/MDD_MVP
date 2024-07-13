package com.popcodelab.mddapi.dto;

import lombok.Data;

/**
 * The SignInDto class represents the data required for user sign-in.
 * It contains the user's email or username and password.
 *
 * @author Pignon Pierre-Olivier
 * @version 1.0
 */
@Data
public class SignInDto {
    private String emailOrUsername;
    private String password;
}
