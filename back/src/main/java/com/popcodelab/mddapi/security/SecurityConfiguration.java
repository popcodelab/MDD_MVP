package com.popcodelab.mddapi.security;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.spec.SecretKeySpec;

@Configuration
@Log4j2
public class SecurityConfiguration {

    @Value("${application.security.jwt.secret}")
    private String jwtSecret;

    /**
     * The WHITE_LIST_URL variable represents a list of URLs that are allowed to bypass authentication
     * and authorization checks in the application.
     * These URLs are considered to be public and accessible to all users without any restrictions.
     *
     * @see SecurityConfiguration
     */
    private static final String[] WHITE_LIST_SWAGGER_URL = {
            "/v3/api-docs/**",
            "/swagger-ui/**"
    };



    @Bean
    public JwtEncoder jwtEncoder() {
        return new NimbusJwtEncoder(new ImmutableSecret<>(this.jwtSecret.getBytes()));
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        log.debug("Secret key : {} ", jwtSecret);
        SecretKeySpec secretKey = new SecretKeySpec(this.jwtSecret.getBytes(), "HmacSHA256");
        return NimbusJwtDecoder.withSecretKey(secretKey).macAlgorithm(MacAlgorithm.HS256).build();
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.authorizeHttpRequests(authorize -> authorize
                .requestMatchers(WHITE_LIST_SWAGGER_URL)
                .permitAll()
                .anyRequest().authenticated());
        http.oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.decoder(jwtDecoder())));
        return http.build();
    }
}
