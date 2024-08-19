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
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    /**
     * The PostRepository variable represents a repository for managing post data.
     */
    private final PostRepository postRepository;
    /**
     * The UserRepository variable represents a repository for managing user data.
     */
    private final UserRepository userRepository;
    /**
     * The TopicRepository variable represents a repository for managing topic data.
     */
    private final TopicRepository topicRepository;
    /**
     * Represents an instance of ModelMapper used for object mapping.
     */
    private final ModelMapper modelMapper;

    /**
     * Retrieves a list of PostDto objects representing posts from user topics.
     *
     * @param topicIds a list of topic IDs
     * @return a list of PostDto objects representing posts from user topics
     */
    public List<PostDto> getPostsFromUserTopics(final List<Long> topicIds) {
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

    /**
     * Retrieves a post by its ID.
     *
     * @param id the ID of the post to retrieve
     * @return an Optional containing the PostDto associated with the given ID, or an empty Optional if no post is found
     */
    @Override
    public Optional<PostDto> getPostById(final Long id) {
        return postRepository.findById(id)
                .map(this::mapPostToPostDto);
    }

    private PostDto mapPostToPostDto(final Post post) {
        User author = userRepository.findById(post.getUserId())
                .orElseThrow(() -> prepareEntityNotFoundException("User", post.getUserId()));
        Topic topic = topicRepository.findById(post.getTopicId())
                .orElseThrow(() -> prepareEntityNotFoundException("Topic", post.getTopicId()));

        PostDto postDto = modelMapper.map(post, PostDto.class);
        postDto.setUsername(author.getUsername());
        postDto.setTopicTitle(topic.getTitle());
        log.debug("Post Id : {} has been retrieved > Title : {}", postDto.getId(), postDto.getTitle());
        return postDto;
    }

    private EntityNotFoundException prepareEntityNotFoundException(final String entityType, final Long id) {
        log.warn(entityType + " not found with id " + id);
        return new EntityNotFoundException(entityType + " not found with id " + id);
    }


    /**
     * Creates a new post based on the provided PostDto object.
     *
     * @param postDto The PostDto object that contains the information of the post to be created.
     * @return The newly created PostDto object with additional information like author username and topic title.
     * @throws EntityNotFoundException If the provided userId or topicId does not exist.
     */
    @Override
    public PostDto newPost(final PostDto postDto) {
        Long userId = postDto.getUserId();
        Long topicId = postDto.getTopicId();
        validateUserAndTopicExistence(userId, topicId);
        Post post = modelMapper.map(postDto, Post.class);
        post.setUserId(userId);
        post.setTopicId(topicId);
        Post newPost = postRepository.save(post);
        log.debug("The post with title '{}' and ID {} was saved successfully", post.getTitle(), post.getId());
        User author;
        Topic topic;
        try {
            author = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found"));
            topic = topicRepository.findById(topicId).orElseThrow(() -> new EntityNotFoundException("Topic not found"));
        } catch (EntityNotFoundException e) {
            log.error("The post with title '{}' and ID {} could not be created as it references a non-existing user or topic", postDto.getTitle(), postDto.getId());
            throw e;
        }
        PostDto newPostDto = modelMapper.map(newPost, PostDto.class);
        newPostDto.setUsername(author.getUsername());
        newPostDto.setTopicTitle(topic.getTitle());
        return newPostDto;
    }

    /**
     * Validates the existence of a user and topic.
     *
     * @param userId  the ID of the user
     * @param topicId the ID of the topic
     * @throws EntityNotFoundException if the user or topic does not exist
     */
    private void validateUserAndTopicExistence(final Long userId, final Long topicId) {
        if (!userRepository.existsById(userId) || !topicRepository.existsById(topicId)) {
            log.error("Attempted to create a post for non-existent user or topic. User ID: {}, Topic ID: {}", userId, topicId);
            throw new EntityNotFoundException("User or topic does not exist");
        }
    }
}
