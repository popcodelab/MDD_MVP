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

    private final TopicRepository topicRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<TopicDto> getAllTopics() {
        log.info("Retrieving all the topics");
        return topicRepository.findAll().stream()
                .map(topic -> modelMapper.map(topic, TopicDto.class))
                .collect(Collectors.toList());
    }
}
