package com.popcodelab.mddapi.dto.authentication;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;


/**
 * The JwtDto class represents a JSON Web Token (JWT) data transfer object.
 * It contains the access token used for authentication.
 */
@Data
@Builder
public class JwtDto {

    /**
     * This variable represents the access token for authentication.
     * An access token is a credential that is used to access protected resources.
     */
    @JsonProperty("token")
    private String accessToken;
}
