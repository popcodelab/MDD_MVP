package com.popcodelab.mddapi.services.impl;

import com.popcodelab.mddapi.dto.authentication.UserDto;
import com.popcodelab.mddapi.entities.User;
import com.popcodelab.mddapi.repositories.TopicRepository;
import com.popcodelab.mddapi.repositories.UserRepository;
import com.popcodelab.mddapi.services.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * The UserServiceImpl class is an implementation of the UserService interface.
 * It provides methods for retrieving information about the logged user.
 */
@Service
@Log4j2
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    /**
     * Repository for managing user data.
     */
    private final UserRepository userRepository;

    /**
     * The topicRepository is an instance of the TopicRepository interface that is responsible for providing database operations
     * for the Topic entity in the system. It is used to interact with the database and perform CRUD (Create, Read, Update, Delete) operations
     * on Topic entities.
     *
     */
    private final TopicRepository topicRepository;

    /**
     * ModelMapper instance to map a Dto to an Entity and vice versa.
     */
    @Autowired
    private ModelMapper modelMapper;

    /**
     * Retrieves the information about the logged user.
     *
     * @param authentication The authentication object representing the logged user.
     * @return The UserDto object containing information about the logged user.
     */
    public UserDto getLoggedUser(Authentication authentication) {
        log.debug("Looking for the user who logs with : {}", authentication.getName());
        User user = findUserByNameOrEmail(authentication.getName());
        UserDto userDto = modelMapper.map(user, UserDto.class);
        log.debug("The user {} - {} has been retrieved", user.getUsername(), user.getEmail());
        return userDto;
    }

    /**
     * Finds a user by name or email identifier.
     *
     * @param identifier The name or email identifier of the user.
     * @return The User object matching the name or email identifier.
     * @throws UsernameNotFoundException If the user is not found.
     */
    private User findUserByNameOrEmail(String identifier) {
        User user = userRepository.findByEmail(identifier);
        if (user == null) {
            user = userRepository.findByUsername(identifier);
        }
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return user;
    }

    /**
     * Unsubscribes a user from a topic.
     *
     * @param topicId            the ID of the topic to unsubscribe from
     * @param authentication    the authentication object of the logged-in user
     * @return the UserDto object representing the logged-in user after the unsubscription
     */
    public UserDto unsubscribesTopic(Long topicId, Authentication authentication) {
        UserDto loggedUserDto = getLoggedUser(authentication);
        User user = verifyUser(loggedUserDto);
        verifyTopicExists(topicId);
        unsubscribeFromTopic(user, topicId);
        userRepository.save(user);
        return modelMapper.map(user, UserDto.class);
    }

    /**
     * Verifies the existence of a user based on the given UserDto object.
     *
     * @param loggedUserDto the UserDto object containing user information.
     * @return the corresponding User object if found.
     * @throws EntityNotFoundException if the user is not found in the repository.
     */
    private User verifyUser(UserDto loggedUserDto) {
        return userRepository.findById(loggedUserDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    /**
     * Verifies if a topic with the given ID exists.
     *
     * @param topicId the ID of the topic to be verified
     * @throws EntityNotFoundException if the topic with the given ID does not exist
     */
    private void verifyTopicExists(Long topicId) {
        if (!topicRepository.existsById(topicId)) {
            throw new EntityNotFoundException("Topic not found");
        }
    }

    /**
     * Unsubscribes a user from a topic.
     *
     * @param user The User object representing the user who wants to unsubscribe.
     * @param topicId The ID of the topic the user wishes to unsubscribe from.
     * @throws IllegalArgumentException If the user is not currently subscribed to the specified topic.
     */
    private void unsubscribeFromTopic(User user, Long topicId) {
        if (!user.getSubscribedTopicIds().contains(topicId)) {
            throw new IllegalArgumentException("User did not subscribed to this topic");
        }
        user.getSubscribedTopicIds().remove(topicId);
    }

}
