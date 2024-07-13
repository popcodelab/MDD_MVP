package com.popcodelab.mddapi.controllers;

import com.popcodelab.mddapi.dto.SignInDto;
import com.popcodelab.mddapi.dto.UserDto;
import com.popcodelab.mddapi.dto.UserToRegisterDto;
import com.popcodelab.mddapi.security.services.JwtService;
import com.popcodelab.mddapi.services.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
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
public class AuthenticationController {
    /**
     * The AuthenticationServiceImpl class provides methods for user authentication and registration.
     */
    @Autowired
    private AuthenticationService authenticationService;

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
            @ApiResponse(responseCode = "201", description = "Created : User registered successfully", content = @Content(schema = @Schema(implementation = UserDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad request")})
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ResponseEntity<?> register(@RequestBody UserToRegisterDto userToRegisterDto) {
        try {
            UserDto userDto = this.authenticationService.registerUser(userToRegisterDto);
            return ResponseEntity.ok(userDto);
        } catch (IllegalArgumentException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponseEntity<Map<String, String>> login(@RequestBody SignInDto signInDto) {
        try {
            Authentication authentication = this.authenticationService.authenticateUser(signInDto);
            String token = jwtService.generateToken(authentication);
            return ResponseEntity.ok(Collections.singletonMap("token", token));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
