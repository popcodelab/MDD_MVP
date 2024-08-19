package com.popcodelab.mddapi.services.impl;

import com.popcodelab.mddapi.dto.comment.CommentDto;
import com.popcodelab.mddapi.entities.Comment;
import com.popcodelab.mddapi.entities.Post;
import com.popcodelab.mddapi.entities.User;
import com.popcodelab.mddapi.repositories.CommentRepository;
import com.popcodelab.mddapi.repositories.PostRepository;
import com.popcodelab.mddapi.repositories.UserRepository;
import com.popcodelab.mddapi.services.CommentService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * The CommentServiceImpl class implements the CommentService interface,
 * providing methods for retrieving and adding comments to a post.
 * It is annotated with @Service, indicating that it is a service component
 * that should be automatically detected and registered as a bean in the Spring context.
 * <p>
 * This class uses the CommentRepository, UserRepository, and PostRepository interfaces
 * to retrieve data from the database. It also uses the ModelMapper library to convert
 * Comment entities to CommentDto objects for the API response.
 * <p>
 * The getAllCommentsByPostId method retrieves all comments for a given post ID.
 * It uses the CommentRepository's findCommentsByPostId method to retrieve the comments
 * from the database, and then converts them to CommentDto objects using the convertToDto method.
 * <p>
 * The convertToDto method converts a Comment entity to a CommentDto object,
 * also setting up the author of the comment by retrieving the corresponding User entity
 * from the UserRepository.
 * <p>
 * The addNewComment method adds a new comment to a post. It first retrieves the user
 * and post entities specified in the commentDto from the UserRepository and PostRepository,
 * and then saves the new comment using the CommentRepository. It also updates the list
 * of comment IDs in the post entity and saves it back to the database. Finally, it converts
 * the newly saved comment to a CommentDto object and returns it.
 */
@Service
@Log4j2
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Autowired
    private ModelMapper modelMapper;


    /**
     * Retrieves all comments for a given post ID.
     *
     * @param postId the ID of the post
     * @return a list of CommentDto containing all comments for the specified post ID
     */
    @Override
    public List<CommentDto> getAllCommentsByPostId(final Long postId) {
        return commentRepository.findCommentsByPostId(postId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * Convert a Comment to CommentDto, also setting up the author of the post
     **/
    private CommentDto convertToDto(Comment comment) {
        log.debug("Convert comment id {} to DTO ", comment.getId());
        User user = userRepository.findById(comment.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found with id " + comment.getUserId()));
        CommentDto commentDto = modelMapper.map(comment, CommentDto.class);
        commentDto.setUsername(user.getUsername());
        return commentDto;
    }

    /**
     * Adds a new comment to a post.
     *
     * @param commentDto the CommentDto object containing the details of the comment to be added
     * @return the CommentDto object representing the newly added comment
     * @throws EntityNotFoundException if the user or post specified in the commentDto doesn't exist
     */
    @Override
    public CommentDto addNewComment(CommentDto commentDto) {
        User user = userRepository.findById(commentDto.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found with id " + commentDto.getUserId()));

        Post post = postRepository.findById(commentDto.getPostId())
                .orElseThrow(() -> new EntityNotFoundException("post not found with id " + commentDto.getPostId()));

        Comment comment = modelMapper.map(commentDto, Comment.class);
        comment.setUserId(user.getId());
        Comment newComment = commentRepository.save(comment);
        log.debug("Comment : {} saved", comment.getId());
        post.getCommentIds().add(newComment.getId());
        postRepository.save(post);

        CommentDto newCommentDto = modelMapper.map(newComment, CommentDto.class);
        newCommentDto.setUsername(user.getUsername());
        log.debug("The comment id {} has been add to Post Id : {} by {} : ",
                newCommentDto.getId(), newCommentDto.getUsername());

        return newCommentDto;
    }
}
