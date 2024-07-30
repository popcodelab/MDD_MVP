package com.popcodelab.mddapi.security.services;

import org.springframework.security.core.Authentication;

/**
 * The JwtService interface provides a method for generating JWT tokens for authentication.
 *
 * @author Pignon Pierre-Olivier
 * @version 1.0
 */
public interface JwtService {
    /**
     * Generates a JWT token for the given authentication.
     *
     * @param authentication the authentication object containing the user's credentials
     * @return the generated JWT token as a string
     */
    String generateToken(Authentication authentication);
}
