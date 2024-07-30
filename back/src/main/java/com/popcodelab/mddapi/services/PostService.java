package com.popcodelab.mddapi.services;

import com.popcodelab.mddapi.dto.post.PostDto;

import java.util.List;

public interface PostService {

    List<PostDto> getPostsFromUserTopics(List<Long> topicIds);
}
