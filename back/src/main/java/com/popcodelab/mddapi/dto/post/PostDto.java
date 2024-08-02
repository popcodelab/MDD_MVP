package com.popcodelab.mddapi.dto.post;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostDto {
    private Long id;
    private String title;
    private String content;
    private Long userId;
    private String username;
    private Long topicId;
    private String topicTitle;
    @JsonProperty("created_at")
    private LocalDateTime createdAt;
}
