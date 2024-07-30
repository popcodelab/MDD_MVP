package com.popcodelab.mddapi.services;


import com.popcodelab.mddapi.dto.topic.TopicDto;

import java.util.List;

public interface TopicService {
    /**
     * Retrieves all topics.
     *
     * @return A list of TopicDto objects representing the retrieved topics.
     */
    List<TopicDto> getAllTopics();
}
