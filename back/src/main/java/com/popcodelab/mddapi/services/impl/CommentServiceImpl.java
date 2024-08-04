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
    public List<CommentDto> getAllCommentsByPostId(Long postId) {
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
