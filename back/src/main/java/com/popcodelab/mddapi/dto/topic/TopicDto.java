package com.popcodelab.mddapi.dto.topic;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The TopicDto class represents a data transfer object for a topic.
 * It contains information such as the topic's id, title, and description.
 *
 * This class is annotated with the @Data and @Builder annotations, which generate
 * getters, setters, equals, hashCode, and toString methods for the class. These
 * annotations also allow for the use of the Builder design pattern to create instances
 * of this class with a fluent API.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TopicDto {
    private Long id;
    private String title;
    private String description;
}
