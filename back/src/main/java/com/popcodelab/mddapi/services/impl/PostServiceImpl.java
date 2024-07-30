package com.popcodelab.mddapi.services.impl;

import com.popcodelab.mddapi.dto.post.PostDto;
import com.popcodelab.mddapi.entities.Post;
import com.popcodelab.mddapi.entities.Topic;
import com.popcodelab.mddapi.entities.User;
import com.popcodelab.mddapi.repositories.PostRepository;
import com.popcodelab.mddapi.repositories.TopicRepository;
import com.popcodelab.mddapi.repositories.UserRepository;
import com.popcodelab.mddapi.services.PostService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final TopicRepository topicRepository;
    private final ModelMapper modelMapper;

    /**
     * Retrieves a list of PostDto objects representing posts from user topics.
     *
     * @param topicIds a list of topic IDs
     * @return a list of PostDto objects representing posts from user topics
     */
    public List<PostDto> getPostsFromUserTopics(List<Long> topicIds){
        List<Post> allPosts = postRepository.findByTopicIds(topicIds);
        Set<Long> userIds = allPosts.stream().map(Post::getUserId).collect(Collectors.toSet());
        Set<Long> topicIdsInPosts = allPosts.stream().map(Post::getTopicId).collect(Collectors.toSet());

        List<User> allUsers = userRepository.findByIds(new ArrayList<>(userIds));
        List<Topic> allTopics = topicRepository.findByIds(new ArrayList<>(topicIdsInPosts));

        Map<Long, User> userMap = allUsers.stream().collect(Collectors.toMap(User::getId, user -> user));
        Map<Long, Topic> topicMap = allTopics.stream().collect(Collectors.toMap(Topic::getId, topic -> topic));

        return allPosts.stream().map(post -> {
            User author = userMap.get(post.getUserId());
            Topic topic = topicMap.get(post.getTopicId());

            PostDto postDto = modelMapper.map(post, PostDto.class);
            postDto.setUsername(author.getUsername());
            postDto.setTopicTitle(topic.getTitle());

            return postDto;
        }).collect(Collectors.toList());
    }
}
