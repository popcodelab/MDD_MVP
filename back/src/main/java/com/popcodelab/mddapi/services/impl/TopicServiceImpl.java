package com.popcodelab.mddapi.services.impl;

import com.popcodelab.mddapi.dto.topic.TopicDto;
import com.popcodelab.mddapi.repositories.TopicRepository;
import com.popcodelab.mddapi.services.TopicService;
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
public class TopicServiceImpl implements TopicService {

    /**
     * The topicRepository variable is an instance of the TopicRepository interface.
     * It is responsible for providing database operations for the Topic entity in the system.
     *
     * The TopicRepository interface extends the JpaRepository interface, which provides basic CRUD operations.
     *
     * @see TopicRepository
     * @author Pignon Pierre-Olivier
     */
    private final TopicRepository topicRepository;

    /**
     * The modelMapper variable is an instance of the ModelMapper class.
     * It is used for mapping between objects of different types.
     *
     * This variable is autowired using the @Autowired annotation, which injects an instance
     * of the ModelMapper class from the Spring application context.
     *
     * @see ModelMapper
     * @see <a href="https://modelmapper.org/">ModelMapper Documentation</a>
     */
    @Autowired
    private ModelMapper modelMapper;

    /**
     * Retrieves all topics.
     *
     * @return A list of TopicDto objects representing the retrieved topics.
     */
    public List<TopicDto> getAllTopics() {
        log.info("Retrieving all the topics");
        return topicRepository.findAll().stream()
                .map(topic -> modelMapper.map(topic, TopicDto.class))
                .collect(Collectors.toList());
    }
}
