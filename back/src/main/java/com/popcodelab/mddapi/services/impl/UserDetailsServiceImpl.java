package com.popcodelab.mddapi.services.impl;

import com.popcodelab.mddapi.entities.User;
import com.popcodelab.mddapi.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * UserDetailsServiceImpl is a service class that implements the UserDetailsService interface.
 * It is responsible for loading a User with the specified username.
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    /**
     * The userRepository variable represents a UserRepository object that is used to interact with the user entities in the system.
     * It is a final variable, meaning that its reference cannot be changed once initialized.
     * The userRepository variable is used in the UserDetailsServiceImpl class to load a User with the specified username.
     *
     * @see UserRepository
     * @see UserDetailsServiceImpl
     */
    private final UserRepository userRepository;

    /**
     * This is a constructor for the UserDetailsServiceImpl class. It initializes the class with a UserRepository object.
     *
     * @param userRepository the UserRepository object used to interact with the user entities in the system
     */
    public UserDetailsServiceImpl(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * This method is responsible for loading a User with the specified username.
     *
     * @param username the username to load the User for
     * @return the UserDetails representing the loaded User
     * @throws UsernameNotFoundException if the User is not found
     */
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username);
        if (user == null) {
            user = userRepository.findByUsername(username);
        }
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        user.getSubscribedTopicIds().size();
        return org.springframework.security.core.userdetails.User.withUsername(user.getEmail())
                .password(user.getPassword())
                .authorities(new ArrayList<>())
                .build();
    }
}

