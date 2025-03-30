package com.fitnycrm.common.rest.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

/**
 * Represents a standardized error response for API errors.
 *
 * @param message The high-level error message (e.g., "Bad Request", "Not Found")
 * @param errors  List of specific validation errors, if any
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponse(
        String message,
        List<ValidationError> errors
) {
    /**
     * Creates an error response with just a message.
     */
    public static ErrorResponse of(String message) {
        return new ErrorResponse(message, null);
    }

    /**
     * Creates an error response with a message and validation errors.
     */
    public static ErrorResponse of(String message, List<ValidationError> errors) {
        return new ErrorResponse(message, errors);
    }
} 