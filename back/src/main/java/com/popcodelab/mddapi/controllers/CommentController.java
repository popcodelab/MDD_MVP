package com.popcodelab.mddapi.controllers;

import com.popcodelab.mddapi.dto.comment.CommentDto;
import com.popcodelab.mddapi.dto.topic.TopicDto;
import com.popcodelab.mddapi.services.CommentService;
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
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * The CommentController class handles HTTP requests related to comments.
 */
@RestController
@RequestMapping("/api/posts/{postId}/comments")
@Log4j2
@Tag(name = "Comment", description = "The Comments API. Includes all operations related to Comment.")
public class CommentController {

    /**
     * Represents a CommentService object.
     *
     * The CommentService is responsible for managing comments in the system.
     *
     * This variable is declared as private and final to ensure that it cannot be modified
     * or accessed externally. It should be initialized upon object creation and can be
     * accessed through getter methods.
     */
    private final CommentService commentService;

    /**
     * CommentController is responsible for handling HTTP requests related to comments.
     *
     * @param commentService The CommentService used to interact with the comments.
     */
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    /**
     * Retrieves all comments belonging to a specific post by its ID.
     *
     * @param postId The ID of the post to retrieve comments from
     * @return A ResponseEntity containing a list of CommentDto objects representing the comments
     */
    @GetMapping
    @Operation(summary = "Get all comments by post ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK : List of topics found",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = CommentDto.class)))),
            @ApiResponse(responseCode = "401", description = "Unauthorized : the user must authenticate itself to get" +
                    " the requested response"),
            @ApiResponse(responseCode = "404", description = "Not Found : No topics found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error : An unexpected error occurred"
            )
    })
    public ResponseEntity<List<CommentDto>> getAllCommentsByPostId(@PathVariable Long postId) {
        List<CommentDto> comments = commentService.getAllCommentsByPostId(postId);
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }

    /**
     * Adds a new comment to a post
     *
     * @param postId     The ID of the post to add the comment to
     * @param commentDTO The CommentDto object containing the details of the comment
     * @return The ResponseEntity containing the created CommentDto object
     */
    @Operation(summary = "Adds a new comment to a post")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Comment added successfully",
                    content = @Content(schema = @Schema(implementation = CommentDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad request, invalid input"),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized : The request lacks valid authentication credentials"
            ),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<CommentDto> addNewCommentToPost(@PathVariable Long postId, @RequestBody CommentDto commentDTO) {
        commentDTO.setPostId(postId);
        CommentDto createdComment = commentService.addNewComment(commentDTO);
        return new ResponseEntity<>(createdComment, HttpStatus.CREATED);
    }
}
