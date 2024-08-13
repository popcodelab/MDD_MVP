package com.popcodelab.mddapi.controllers;


import com.popcodelab.mddapi.dto.authentication.UserDto;
import com.popcodelab.mddapi.dto.post.PostDto;
import com.popcodelab.mddapi.services.PostService;
import com.popcodelab.mddapi.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * The PostController class handles the API endpoints related to post management.
 * It provides methods for retrieving, creating, and updating posts.
 */
@RestController
@RequestMapping("/api/posts")
@Log4j2
@Tag(name = "Post", description = "The Posts API. Includes all operations related to post management.")
public class PostController {

    /**
     * The userService variable is
     */
    private final UserService userService;

    /**
     * The postService variable represents an instance of the PostService class.
     */
    private final PostService postService;

    /**
     * The PostController class is responsible for handling requests related to posts.
     */
    public PostController(PostService postService, UserService userService) {
        this.postService = postService;
        this.userService = userService;
    }

    /**
     * Retrieves the posts for the topics subscribed by the authenticated user.
     *
     * @param authentication the authentication object representing the currently authenticated user
     * @return a ResponseEntity containing a list of PostDto objects if successful, or an error response otherwise
     */
    @GetMapping
    @Operation(summary = "Get posts for subscribed topics")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successfully retrieved list of posts",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = PostDto.class)))
            ),
            @ApiResponse(responseCode = "401",
                    description = "You are not authorized to view the resource"),
            @ApiResponse(responseCode = "403",
                    description = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(responseCode = "404",
                    description = "The resource you were trying to reach is not found"),
            @ApiResponse(responseCode = "500",
                    description = "Internal error")
    })
    public ResponseEntity<List<PostDto>> getPostsFromUserTopics(final Authentication authentication) {
        ResponseEntity<List<Long>> responseEntity = getUserSubscribedTopicsIds(authentication);
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            List<Long> topicIds = responseEntity.getBody();
            List<PostDto> posts = postService.getPostsFromUserTopics(topicIds);
            return new ResponseEntity<>(posts, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(responseEntity.getStatusCode());
        }
    }

    /**
     * Retrieves the list of topic IDs that a user is subscribed to.
     *
     * @param authentication the authentication object representing the logged-in user
     * @return a ResponseEntity object containing the list of subscribed topic IDs, or a 401 Unauthorized status if the user is not authenticated
     */
    private ResponseEntity<List<Long>> getUserSubscribedTopicsIds(final Authentication authentication) {
        UserDto userDto = userService.getLoggedUser(authentication);
        return new ResponseEntity<>(userDto.getSubscribedTopicIds(), HttpStatus.OK);
    }

    /**
     * Retrieves a post by its id.
     *
     * @param id The id of the post to retrieve.
     * @return The ResponseEntity containing the retrieved post if found, otherwise a ResponseEntity with status code NOT_FOUND.
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get post by its Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Success|OK",
                    content = @Content(schema = @Schema(implementation = UserDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<PostDto> getPostById(final @PathVariable Long id) {
        Optional<PostDto> optionalPost = postService.getPostById(id);
        return optionalPost.map(postDto -> new ResponseEntity<>(postDto, HttpStatus.OK)).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }


    /**
     * !
     * Creates a new Post.
     *
     * @param postDto the PostDto object representing the post to be created
     * @return the ResponseEntity containing the newly created PostDto object and the HTTP status code 201 (Created)
     */
    @PostMapping
    @Operation(summary = "Creates a new Post")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Post created successfully",
                    content = @Content(schema = @Schema(implementation = PostDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad request, invalid input"),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized : The request lacks valid authentication credentials"
            ),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<PostDto> newPost(final @RequestBody PostDto postDto) {
        PostDto newPost = postService.newPost(postDto);
        return new ResponseEntity<>(newPost, HttpStatus.CREATED);
    }
}
