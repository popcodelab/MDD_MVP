package com.popcodelab.mddapi.services;

import com.popcodelab.mddapi.dto.post.PostDto;

import java.util.List;

public interface PostService {

    /**
     * Retrieves a list of posts from user topics based on the provided topic IDs.
     *
     * @param topicIds The list of topic IDs to filter the posts.
     * @return A list of PostDto objects representing the posts from user topics.
     */
    List<PostDto> getPostsFromUserTopics(List<Long> topicIds);

    /**
     * Creates a new post based on the provided PostDto object.
     *
     * @param postDto The PostDto object that contains the information of the post to be created.
     * @return The PostDto object representing the newly created post.
     */
    PostDto newPost(PostDto postDto);
}
