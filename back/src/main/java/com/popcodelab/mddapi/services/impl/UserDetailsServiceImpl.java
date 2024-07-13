package com.popcodelab.mddapi.services.impl;

import com.popcodelab.mddapi.entities.User;
import com.popcodelab.mddapi.repositories.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * The UserDetailsServiceImpl class implements the UserDetailsService interface
 * to provide user details for authentication and authorization purposes.
 * It loads a user by username or email from the UserRepository
 * and returns a UserDetails object representing the user.
 */
@Service
@Log4j2
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Loads a user by username or email.
     *
     * @param usernameOrEmail the username of the user to load
     * @return a UserDetails object representing the user
     * @throws UsernameNotFoundException if the user is not found
     */
    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        // Attempt to find the user by email
        User user = userRepository.findByEmail(usernameOrEmail);
        // If not found by email, attempt to find by username
        if (user == null) {
            user = userRepository.findByUsername(usernameOrEmail);
        }
        // If the user is still not found, log a warning and throw an exception
        if (user == null) {
            log.warn("User {} not found !", usernameOrEmail);
            throw new UsernameNotFoundException("User not found");
        }
        // Log the username of the found user
        user.getSubscribedTopicIds().size();
        log.info("Log user {}", user.getUsername());
        // Return the user details required by Spring Security
        return org.springframework.security.core.userdetails.User.withUsername(user.getEmail())
                .password(user.getPassword())
                .authorities(new ArrayList<>())
                .build();
    }
}

