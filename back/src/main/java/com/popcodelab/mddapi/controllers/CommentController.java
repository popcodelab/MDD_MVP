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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts/{postId}/comments")
@Log4j2
@Tag(name = "Comment", description = "The Comments API. Includes all operations related to Comment.")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping
    @Operation(summary = "Get all comments by post ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK : List of topics found",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = TopicDto.class)))),
            @ApiResponse(responseCode = "401", description = "Unauthorized : the user must authenticate itself to get" +
                    " the requested response"),
            @ApiResponse(responseCode = "404", description = "Not Found : No topics found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error : An unexpected error occurred"
            )
    })
    public List<CommentDto> getAllCommentsByPostId(final @PathVariable Long postId) {
        return commentService.getAllCommentsByPostId(postId);
    }

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
    public CommentDto addNewCommentToPost(final @PathVariable Long postId, final @RequestBody CommentDto commentDTO) {
        commentDTO.setPostId(postId);
        return commentService.addNewComment(commentDTO);
    }
}
