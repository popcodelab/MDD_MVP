package com.popcodelab.mddapi.services;

import com.popcodelab.mddapi.dto.comment.CommentDto;

import java.util.List;


public interface CommentService {

    List<CommentDto> getAllCommentsByPostId(Long postId);

    CommentDto addNewComment(CommentDto commentDto);

}
