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

import java.util.ArrayList;
import java.util.List;

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
    public UserDto getLoggedUser(final Authentication authentication) {
        log.debug("Looking for the user who logs with : {}", authentication.getName());
        User user = findUserByNameOrEmail(authentication.getName());
        UserDto userDto = modelMapper.map(user, UserDto.class);
        log.debug("The user {} - {} has been retrieved", user.getUsername(), user.getEmail());
        return userDto;
    }

    /**
     * Updates the information of a user.
     *
     * @param userDto       The updated UserDto object containing the new information of the user.
     * @param authentication The authentication object representing the logged user.
     * @return The updated UserDto object.
     */
    public UserDto updateUser(UserDto userDto, Authentication authentication) {
        UserDto loggedUserDto = getLoggedUser(authentication);
        User user = getUserOrThrowNotFound(loggedUserDto);
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        userRepository.save(user);
        log.debug("User Id {} - {} has been saved.",user.getId(),  user.getUsername());
        return modelMapper.map(user, UserDto.class);
    }

    /**
     * Retrieves a user from the repository based on the logged user DTO.
     *
     * @param loggedUserDto the DTO representing the logged user
     * @return the user, if found
     * @throws EntityNotFoundException if the user is not found
     */
    private User getUserOrThrowNotFound(final UserDto loggedUserDto) {
        return userRepository.findById(loggedUserDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    /**
     * Finds a user by name or email identifier.
     *
     * @param identifier The name or email identifier of the user.
     * @return The User object matching the name or email identifier.
     * @throws UsernameNotFoundException If the user is not found.
     */
    private User findUserByNameOrEmail(final String identifier) {
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
     * @param topicId        the ID of the topic to unsubscribe from
     * @param authentication the authentication object of the logged-in user
     * @return the UserDto object representing the logged-in user after the unsubscription
     */
    public UserDto unsubscribesTopic(final Long topicId, final Authentication authentication) {
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
    private User verifyUser(final UserDto loggedUserDto) {
        return userRepository.findById(loggedUserDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    /**
     * Verifies if a topic with the given ID exists.
     *
     * @param topicId the ID of the topic to be verified
     * @throws EntityNotFoundException if the topic with the given ID does not exist
     */
    private void verifyTopicExists(final Long topicId) {
        if (!topicRepository.existsById(topicId)) {
            throw new EntityNotFoundException("Topic not found");
        }
    }

    /**
     * Unsubscribes a user from a topic.
     *
     * @param user    The User object representing the user who wants to unsubscribe.
     * @param topicId The ID of the topic the user wishes to unsubscribe from.
     * @throws IllegalArgumentException If the user is not currently subscribed to the specified topic.
     */
    private void unsubscribeFromTopic(final User user, final Long topicId) {
        if (!user.getSubscribedTopicIds().contains(topicId)) {
            throw new IllegalArgumentException("User did not subscribed to this topic");
        }
        user.getSubscribedTopicIds().remove(topicId);
    }

    /**
     * Subscribes the logged-in user to a given topic.
     *
     * @param topicId        The ID of the topic to subscribe to.
     * @param authentication The authentication object representing the logged-in user.
     * @return The UserDto representing the subscribed user.
     * @throws EntityNotFoundException if the user is not found.
     */
    public UserDto subscribeToTopic(final Long topicId, final Authentication authentication) {
        UserDto currentUserDTO = getLoggedUser(authentication);
        User user = userRepository.findById(currentUserDTO.getId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        validateTopicExists(topicId);
        validateUserAlreadySubscribed(user, topicId);

        List<Long> subscribedTopicIds = new ArrayList<>(user.getSubscribedTopicIds());
        subscribedTopicIds.add(topicId);
        user.setSubscribedTopicIds(subscribedTopicIds);

        userRepository.save(user);
        log.debug("The user {} has subscribed to the topic Id : {}", user.getUsername(), topicId);
        return modelMapper.map(user, UserDto.class);
    }

    /**
     * Validates if a topic exists based on the given topicId.
     *
     * @param topicId the ID of the topic to validate
     * @throws EntityNotFoundException if the topic does not exist
     */
    private void validateTopicExists(Long topicId) {
        if (!topicRepository.existsById(topicId)) {
            throw new EntityNotFoundException("Topic not found");
        }
    }

    /**
     * Validates whether the user has already subscribed to a given topic.
     *
     * @param user    the user object representing the user
     * @param topicId the topic ID to check if the user is subscribed to
     * @throws IllegalArgumentException if the user is already subscribed to the specified topic
     */
    private void validateUserAlreadySubscribed(User user, Long topicId) {
        if (user.getSubscribedTopicIds().contains(topicId)) {
            throw new IllegalArgumentException("User has already subscribed to this topic");
        }
    }
}
