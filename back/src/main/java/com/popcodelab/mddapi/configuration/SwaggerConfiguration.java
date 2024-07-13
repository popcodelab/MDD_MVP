package com.popcodelab.mddapi.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * This class provides the configuration for Swagger documentation.
 *
 * @author Pignon Pierre-Olivier
 * @version 1.0
 */
@Configuration
public class SwaggerConfiguration {

    /**
     * This method returns an instance of OpenAPI.
     *
     * @return The OpenAPI instance
     */
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI().addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
                .components(new Components().addSecuritySchemes("Bearer Authentication", createAPIKeyScheme()))
                .info(new Info().title("MDD - MVP Rest API")
                        .description("Documentation de l'API Rest de l'application MDD (MVP).")
                        .version("1.0").contact(new Contact().name("Pignon Pierre-Olivier")));
    }

    /**
     * Creates a security scheme for API key authentication.
     *
     * @return The created security scheme
     */
    private SecurityScheme createAPIKeyScheme() {
        return new SecurityScheme().type(SecurityScheme.Type.HTTP)
                .bearerFormat("JWT")
                .scheme("bearer");
    }
}
