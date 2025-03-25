package com.fitnycrm.common.rest.model;

/**
 * Represents a single field validation error.
 *
 * @param field The name of the field that failed validation
 * @param message The validation error message
 */
public record ValidationError(
    String field,
    String message
) {} 