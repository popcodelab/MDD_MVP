package com.popcodelab.mddapi.dto.authentication;

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
    /**
     * The emailOrUsername variable represents the email or username of a user.
     * It is used in the SignInDto class to store the user's email or username for sign-in purposes.
     */
    private String emailOrUsername;

    /**
     * The password variable represents the password of a user.
     * It is used in the SignInDto class to store the user's password for sign-in purposes.
     */
    private String password;
}
