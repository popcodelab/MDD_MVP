package com.popcodelab.mddapi.security.services;

import org.springframework.security.core.Authentication;

/**
 * The JwtService interface provides a method for generating JWT tokens for authentication.
 *
 * @author Pignon Pierre-Olivier
 * @version 1.0
 */
public interface JwtService {
    String generateToken(Authentication authentication);
}
