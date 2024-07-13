package com.popcodelab.mddapi.configuration;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for configuring the ModelMapper bean.
 *
 * @author Pignon Pierre-Olivier
 * @version 1.0
 */
@Configuration
public class ModelMapperConfiguration {
    /**
     * Creates and configures a ModelMapper bean.
     *
     * @return The ModelMapper instance.
     */
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return modelMapper;
    }
}
