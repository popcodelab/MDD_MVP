package com.popcodelab.mddapi.services.impl;

import com.popcodelab.mddapi.dto.authentication.SignInDto;
import com.popcodelab.mddapi.dto.authentication.UserDto;
import com.popcodelab.mddapi.dto.authentication.UserToRegisterDto;
import com.popcodelab.mddapi.entities.User;
import com.popcodelab.mddapi.repositories.UserRepository;
import com.popcodelab.mddapi.services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * The AuthenticationServiceImpl class implements the AuthenticationService interface
 * and provides methods for user authentication and registration.
 */
@Service
@Log4j2
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    /**
     * Repository for managing user data.
     */
    private final UserRepository userRepository;

    /**
     * The passwordEncoder variable is an instance of the BCryptPasswordEncoder class that is used
     * to encode passwords using the BCrypt hashing algorithm.
     */
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    /**
     * ModelMapper instance to map a Dto to an Entity and vice versa.
     */
    @Autowired
    private ModelMapper modelMapper;

    /**
     * Authenticates a user with the provided email, username, and password.
     *
     * @param signInDto The SignInDto object containing the user's email or username and password.
     * @return The authenticated user's authentication object.
     * @throws UsernameNotFoundException If the user is not found in the database.
     * @throws BadCredentialsException If the provided credentials are invalid.
     */
    public Authentication authenticateUser(SignInDto signInDto) {
        log.debug("Authenticate user : {}", signInDto.getEmailOrUsername());
        User user = userRepository.findByEmail(signInDto.getEmailOrUsername());
        if (user == null) {
            user = userRepository.findByUsername(signInDto.getEmailOrUsername());
        }
        if (user == null) {
            log.warn("Invalid credentials");
            throw new UsernameNotFoundException("Invalid credentials");
        }
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(signInDto.getEmailOrUsername(), signInDto.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.info("User has been authenticated.");
            return authentication;
        } catch (AuthenticationException e) {
            log.error("Bad credentials !");
            throw new BadCredentialsException("Bad credentials");
        }
    }

    /**
     * Registers a new user with the provided user information.
     *
     * @param userToRegisterDto The RegisterDto object representing the user information to be registered.
     * @return The UserDto object representing the registered user.
     * @throws IllegalArgumentException if the email is already in use, the username is already in use, or the password is less than 8 characters long.
     */
    public UserDto registerUser(UserToRegisterDto userToRegisterDto) {
        log.debug("Register user : {}", userToRegisterDto);
        User existingUser = userRepository.findByEmail(userToRegisterDto.getEmail());
        if (existingUser != null) {
            throw new IllegalArgumentException("Email is already in use");
        }

        existingUser = userRepository.findByUsername(userToRegisterDto.getUsername());
        if (existingUser != null) {
            throw new IllegalArgumentException("Username is already in use");
        }

        if (userToRegisterDto.getPassword().length() < 8) {
            throw new IllegalArgumentException("Password must be at least 8 characters long");
        }

        User user = new User();
        user.setEmail(userToRegisterDto.getEmail());
        user.setUsername(userToRegisterDto.getUsername());
        user.setPassword(passwordEncoder.encode(userToRegisterDto.getPassword()));
        User savedUser = this.userRepository.save(user);
        UserDto result = this.modelMapper.map(savedUser, UserDto.class);
        log.info("User {} has been registered", result.getEmail());
        return result;
    }
}
