package com.popcodelab.mddapi.controllers;


import com.popcodelab.mddapi.dto.authentication.JwtDto;
import com.popcodelab.mddapi.dto.authentication.SignInDto;
import com.popcodelab.mddapi.dto.authentication.UserDto;
import com.popcodelab.mddapi.dto.authentication.UserToRegisterDto;
import com.popcodelab.mddapi.security.services.JwtService;
import com.popcodelab.mddapi.services.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * The AuthenticationController class provides REST endpoint for user authentication and registration.
 *
 * @author Pignon Pierre-Olivier
 * @version 1.0
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Log4j2
@Tag(name = "Authentication", description = "The Authentication API. Includes all operations related to authentication processes.")
public class AuthenticationController {
    /**
     * The AuthenticationServiceImpl class provides methods for user authentication and registration.
     */
    @Autowired
    private AuthenticationService authenticationService;

    /**
     * An instance of the JwtService class which is used for handling JSON Web Tokens (JWTs).
     *
     * It is autowired and therefore injected using Spring's dependency injection mechanism.
     *
     * This variable can be used to access the methods and functionality provided by the JwtService class.
     *
     * Please refer to the JwtService class documentation for more information on how to use this service.
     */
    @Autowired
    private JwtService jwtService;

    /**
     * Registers a new user.
     *
     * @param userToRegisterDto The UserToRegisterDto object representing the user information to be registered.
     * @return ResponseEntity containing the registered UserDto object or a Bad Request error response.
     */
    @Operation(summary = "Registers a new user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created : User registered successfully",
                            content = @Content(schema = @Schema(implementation = UserDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad request")})
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ResponseEntity<?> register(final @RequestBody UserToRegisterDto userToRegisterDto) {
        try {
            UserDto userDto = this.authenticationService.registerUser(userToRegisterDto);
            return ResponseEntity.ok(userDto);
        } catch (IllegalArgumentException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    /**
     * Authenticates a user based on the provided sign-in credentials.
     *
     * @param signInDto The SignInDto object containing the user's email or username and password.
     * @return ResponseEntity containing a map with a single key "token" and its corresponding value representing
     * the generated JWT token if authentication is successful. Returns
     * a Bad Request error response if authentication fails.
     */
    @Operation(summary = "Authenticates an user.", description = "Provide credentials  to authenticate user " +
            "and receive a token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return the User token - Authenticated successfully",
                    content = @Content(schema = @Schema(implementation = JwtDto.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized")})
    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponseEntity<Map<String, String>> login(final @RequestBody SignInDto signInDto) {
        try {
            Authentication authentication = this.authenticationService.authenticateUser(signInDto);
            String token = jwtService.generateToken(authentication);
            return ResponseEntity.ok(Collections.singletonMap("token", token));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

}
