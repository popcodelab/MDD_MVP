package com.popcodelab.mddapi.controllers;


import com.popcodelab.mddapi.dto.authentication.UserDto;
import com.popcodelab.mddapi.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/me")
@Log4j2
@Tag(name = "User", description = "The Users API. Includes all operations related to user management.")
public class UserController {

    /**
     * The userService variable represents an instance of the UserService interface. It is used in the UserController class to retrieve information about the logged user.
     * The UserService interface provides the getLoggedUser() method, which is responsible for retrieving the logged user's information.
     * The returned value is a UserDto object, which contains details such as the user's ID, username, email, and subscribed topic IDs.
     */
    private final UserService userService;

    /**
     * Constructs a new UserController with the specified UserService.
     *
     * @param userService the UserService used to interact with the user data
     */
    public UserController(final UserService userService) {
        this.userService = userService;
    }

    /**
     * Retrieves information about the logged user.
     *
     * @param authentication The authentication object representing the logged user.
     * @return A ResponseEntity containing the UserDto object representing the logged user.
     * @apiNote This method returns an HTTP response with status code 200 (OK) if the user is found.
     * Otherwise, it returns a 401 (Unauthorized) response if the user is not authenticated or a
     * 404 (Not Found) response if the user information cannot be retrieved.
     */
    @GetMapping
    @Operation(summary = "Get logged user.", description = "Get information about the logged user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found", content = @Content(schema = @Schema(implementation = UserDto.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Not Found : the user has not been retrieved"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error : An unexpected error occurred"
            )})
    public ResponseEntity<?> getLoggedUser(final Authentication authentication) {
        log.debug("Retrieving info for login : {}", authentication.getName());
        UserDto userDto = userService.getLoggedUser(authentication);
        log.debug("User found : {} - {}", userDto.getUsername(), userDto.getEmail());
        return ResponseEntity.ok(userDto);
    }


    @DeleteMapping("/topic/{topicId}")
    @Operation(summary = "Deletes a user topic subscription.",
            description = "Removes the link between a user and topic subscription")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "User topic subscription successfully deleted",
                    content = @Content(schema = @Schema(implementation = UserDto.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized : The request lacks valid authentication credentials"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Not Found : The topic subscription could not be found"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal Server Error : An unexpected error occurred"
            )
    })
    public UserDto unsubscribesTopic(final @PathVariable Long topicId, final Authentication authentication) {
        return userService.unsubscribesTopic(topicId, authentication);
    }

    @PostMapping("/topic/{topicId}")
    @Operation(summary = "Creates a new user topic subscription.",
            description = "Adds a link between a user and a topic subscription")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "User topic subscription successfully created",
                    content = @Content(schema = @Schema(implementation = UserDto.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad Request : Invalid topic ID or request body"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized : The request lacks valid authentication credentials"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Not Found : The topic could not be found"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal Server Error : An unexpected error occurred"
            )
    })
    public UserDto subscribesTopic(final @PathVariable Long topicId, final Authentication authentication) {
        return userService.subscribeToTopic(topicId, authentication);
    }
}
