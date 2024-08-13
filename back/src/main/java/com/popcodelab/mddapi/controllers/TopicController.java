package com.popcodelab.mddapi.controllers;

import com.popcodelab.mddapi.dto.topic.TopicDto;
import com.popcodelab.mddapi.services.TopicService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * The TopicController class is responsible for handling requests related to topics.
 */
@RestController
@RequestMapping("/api/topics")
@Log4j2
@Tag(name = "Topic", description = "The Topics API. Includes all operations related to topic management.")
public class TopicController {

    /**
     * The TopicService interface defines the contract for a service that manages topics.
     * It provides methods to retrieve all topics.
     */
    private final TopicService topicService;

    /**
     * The TopicController class is responsible for handling requests related to topics.
     * It provides an endpoint to retrieve all the topics.
     * <p>
     * This class is annotated with @RestController, which marks it as a controller that handles
     * RESTful requests and responses. It is also annotated with @RequestMapping("/api/topics"),
     * which specifies the base URL for all endpoints in this controller.
     * <p>
     * This class depends on the TopicService interface to retrieve the topics data. The dependency
     * is injected through the constructor using the @param tag.
     */
    public TopicController(final TopicService topicService) {
        this.topicService = topicService;
    }

    /**
     * Retrieves all topics.
     *
     * @return A ResponseEntity containing a list of TopicDto objects representing the retrieved topics.
     *         The HTTP status code is set to 200 (OK) if topics are found.
     *         The HTTP status code is set to 401 (Unauthorized) if the user must authenticate itself.
     *         The HTTP status code is set to 404 (Not Found) if no topics are found.
     *         The HTTP status code is set to 500 (Internal Server Error) if an unexpected error occurs.
     */
    @GetMapping
    @Operation(summary = "Get all the topics")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK : List of topics found",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = TopicDto.class)))),
            @ApiResponse(responseCode = "401", description = "Unauthorized : the user must authenticate itself to get" +
                    " the requested response"),
            @ApiResponse(responseCode = "404", description = "Not Found : No topics found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error : An unexpected error occurred"
            )
    })
    public ResponseEntity<List<TopicDto>> getAllTopics() {
        log.info("Seeking for the topics...");
        List<TopicDto> topics = topicService.getAllTopics();
        return new ResponseEntity<>(topics, HttpStatus.OK);
    }
}
