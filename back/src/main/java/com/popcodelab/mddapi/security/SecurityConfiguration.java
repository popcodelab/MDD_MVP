package com.popcodelab.mddapi.security;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.spec.SecretKeySpec;

/**
 * The SecurityConfiguration class is responsible for configuring the security settings
 * of the application. It provides various beans and methods to customize the security
 * behavior.
 *
 * @author Pignon Pierre-Olivier
 * @version 1.0
 */
@Configuration
@Log4j2
@EnableWebSecurity
public class SecurityConfiguration {

    @Value("${application.security.jwt.secret}")
    private String jwtSecret;

    /**
     * Returns an instance of BCryptPasswordEncoder.
     *
     * @return the BCryptPasswordEncoder instance.
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Returns an instance of JwtEncoder.
     *
     * @return the JwtEncoder instance.
     */
    @Bean
    public JwtEncoder jwtEncoder() {
        return new NimbusJwtEncoder(new ImmutableSecret<>(this.jwtSecret.getBytes()));
    }

    /**
     * Returns an instance of JwtDecoder used for decoding JWT tokens.
     *
     * @return the JwtDecoder instance.
     */
    @Bean
    public JwtDecoder jwtDecoder() {
        SecretKeySpec secretKey = new SecretKeySpec(this.jwtSecret.trim().getBytes(), "HmacSHA256");
        return NimbusJwtDecoder.withSecretKey(secretKey).macAlgorithm(MacAlgorithm.HS256).build();
    }

    /**
     * Returns an instance of AuthenticationManager based on the provided AuthenticationConfiguration.
     *
     * @param authenticationConfiguration the AuthenticationConfiguration to be used
     * @return the AuthenticationManager instance
     * @throws Exception if an error occurs while getting the AuthenticationManager
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * The WHITE_LIST_URL variable represents a list of URLs that are allowed to bypass authentication
     * and authorization checks in the application.
     * These URLs are considered to be public and accessible to all users without any restrictions.
     *
     * @see SecurityConfiguration
     */
    private static final String[] WHITE_LIST_SWAGGER_URL = {
            "/",
            "/api/auth/register",
            "/api/auth/login",
            "/swagger-ui/**",
            "/v3/api-docs/**"
    };

    /**
     * Returns the configured SecurityFilterChain object based on the provided HttpSecurity configuration.
     *
     * @param http the HttpSecurity configuration
     * @return the configured SecurityFilterChain object
     * @throws Exception if an error occurs while configuring the filter chain
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        log.debug("SecurityFilterChain called");
        http.csrf(AbstractHttpConfigurer::disable);
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.authorizeHttpRequests(authorize -> authorize
                .requestMatchers(WHITE_LIST_SWAGGER_URL)
                .permitAll()
                .anyRequest().authenticated());
        http.oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.decoder(jwtDecoder())));
        return http.build();
    }
}
