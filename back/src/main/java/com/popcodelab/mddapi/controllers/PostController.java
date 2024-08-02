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

@RestController
@RequestMapping("/api/posts")
@Log4j2
@Tag(name = "Post", description = "The Posts API. Includes all operations related to post management.")
public class PostController {

    private final UserService userService;
    private final PostService postService;

    public PostController(PostService postService, UserService userService){
        this.postService = postService;
        this.userService = userService;
    }
    @GetMapping
    @Operation(summary = "Get articles for subscribed themes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successfully retrieved list of articles",
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
    public ResponseEntity<List<PostDto>> getPostsFromUserTopics(Authentication authentication){
        try {
            List<Long> topicIds = getUserSubscribedTopicsIds(authentication);
            List<PostDto> posts = postService.getPostsFromUserTopics(topicIds);
            return new ResponseEntity<>(posts, HttpStatus.OK);
        } catch (Exception e){
            log.error("Error occurred during getting posts from user topics", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private List<Long> getUserSubscribedTopicsIds(Authentication authentication) {
        UserDto userDto = userService.getLoggedUser(authentication);
        return userDto.getSubscribedTopicIds();
    }


    @PostMapping
    public PostDto createArticle(@RequestBody PostDto postDto) {
        return postService.newPost(postDto);
    }
}
