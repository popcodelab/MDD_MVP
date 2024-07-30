package com.popcodelab.mddapi.services;

import com.popcodelab.mddapi.dto.authentication.UserDto;
import org.springframework.security.core.Authentication;

public interface UserService {

    /**
     * Retrieves the information about the logged user.
     *
     * @param authentication The authentication object representing the logged user.
     * @return The UserDto object containing information about the logged user.
     */
    UserDto getLoggedUser(Authentication authentication);

    UserDto unsubscribesTopic(Long topicId, Authentication authentication);
}
