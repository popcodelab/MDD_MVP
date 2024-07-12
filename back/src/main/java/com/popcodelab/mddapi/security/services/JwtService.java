package com.popcodelab.mddapi.security.services;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

public interface JwtService {
    String generateToken(Authentication authentication);
}
