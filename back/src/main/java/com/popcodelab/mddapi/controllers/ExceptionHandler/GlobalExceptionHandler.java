package com.popcodelab.mddapi.controllers.ExceptionHandler;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

/**
 * GlobalExceptionHandler is a class that handles global exceptions and returns an error response.
 * It is annotated with @ControllerAdvice to specify that it should be applied globally to all controllers.
 * It extends ResponseEntityExceptionHandler to leverage the default exception handling provided by it.
 */
@ControllerAdvice
@Log4j2
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Handles global exceptions and returns an error response.
     *
     * @param ex The exception that occurred.
     * @param request The web request that triggered the exception.
     * @return A ResponseEntity containing an ErrorDetails object.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> globalExceptionHandler(final Exception ex, final WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(), request.getDescription(false));
        log.error("An error has occurred ! : {} > {}", errorDetails.getMessage(), errorDetails.getDetails());
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
