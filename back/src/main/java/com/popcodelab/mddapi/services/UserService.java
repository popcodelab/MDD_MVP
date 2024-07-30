package com.popcodelab.mddapi.services;

import com.popcodelab.mddapi.dto.authentication.UserDto;
import org.springframework.security.core.Authentication;

/**
 * The UserService interface provides methods for retrieving and updating information about users.
 */
public interface UserService {

    /**
     * Retrieves the information about the logged user.
     *
     * @param authentication The authentication object representing the logged user.
     * @return The UserDto object containing information about the logged user.
     */
    UserDto getLoggedUser(final Authentication authentication);

    /**
     * Unsubscribes a user from a topic.
     *
     * @param topicId        The ID of the topic.
     * @param authentication The authentication object representing the logged user.
     * @return The UserDto object representing the updated user information after unsubscribing from the topic.
     */
    UserDto unsubscribesTopic(final Long topicId, final Authentication authentication);

    /**
     * Subscribes a user to a topic.
     *
     * @param topicId        The ID of the topic to subscribe to.
     * @param authentication The authentication object representing the logged user.
     * @return The UserDto object representing the updated user information after subscribing to the topic.
     */
    UserDto subscribeToTopic(final Long topicId, final Authentication authentication);
}
