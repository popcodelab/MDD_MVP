package com.popcodelab.mddapi.security.services.impl;

import com.popcodelab.mddapi.security.services.JwtService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;

/**
 * The JwtServiceImpl class is responsible for generating JWT tokens for authentication.
 * It uses a JwtEncoder to encode the tokens and stores the expiration time as a string.
 * The class implements the JwtService interface.
 *
 * @author Pignon Pierre-Olivier
 * @version 1.0
 */
@Service
public class JwtServiceImpl implements JwtService {
    /**
     * The JwtEncoder variable is used to encode JWT tokens.
     */
    private final JwtEncoder jwtEncoder;

    /**
     * The stringJwtExpiration variable is used to store the value of the JWT expiration time as a string.
     * It is obtained from the application configuration using the application.security.jwt.expiration property.
     * The value represents the duration in milliseconds after which the generated JWT token will expire.
     *
     * @see JwtServiceImpl#getExpiryTime(Instant)
     */
    @Value("${application.security.jwt.expiration}")
    private String stringJwtExpiration;

    public JwtServiceImpl(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    /**
     * Generates a JWT token for the given authentication.
     *
     * @param authentication the authentication object
     * @return the generated JWT token as a string
     */
    public String generateToken(Authentication authentication) {
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(getExpiryTime(now))
                .subject(authentication.getName())
                .build();
        JwtEncoderParameters jwtEncoderParameters = JwtEncoderParameters.from(JwsHeader.with(MacAlgorithm.HS256).build(), claims);
        return this.jwtEncoder.encode(jwtEncoderParameters).getTokenValue();
    }

    /**
     * Calculates the expiry time based on the given start instant.
     *
     * @param startInstant the start instant from which to calculate the expiry time
     * @return the expiry time as an Instant object
     */
    private Instant getExpiryTime(Instant startInstant) {
        return startInstant.plusMillis(Long.parseLong(stringJwtExpiration.trim()));
    }
}
