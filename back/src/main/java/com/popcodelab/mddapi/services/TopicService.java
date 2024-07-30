package com.popcodelab.mddapi.services;


import com.popcodelab.mddapi.dto.topic.TopicDto;

import java.util.List;

public interface TopicService {

    List<TopicDto> getAllTopics();
}
