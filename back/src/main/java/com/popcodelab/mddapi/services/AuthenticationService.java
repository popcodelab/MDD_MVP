package com.popcodelab.mddapi.services;

import com.popcodelab.mddapi.dto.authentication.SignInDto;
import com.popcodelab.mddapi.dto.authentication.UserDto;
import com.popcodelab.mddapi.dto.authentication.UserToRegisterDto;
import org.springframework.security.core.Authentication;

/**
 * The AuthenticationService interface provides methods for user authentication and registration.
 */
public interface AuthenticationService {

    /**
     * Authenticates a user based on the provided sign-in credentials.
     *
     * @param signInDto The SignInDto object containing the user's email or username and password.
     * @return An Authentication object representing the authenticated user.
     */
    Authentication authenticateUser(SignInDto signInDto);

    /**
     * Registers a new user.
     *
     * @param userToRegisterDto The RegisterDto object representing the user information to be registered.
     * @return The JwtDto object containing the access token for the registered user.
     */
    UserDto registerUser(UserToRegisterDto userToRegisterDto);


}
