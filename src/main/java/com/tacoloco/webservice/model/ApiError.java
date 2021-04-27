package com.tacoloco.webservice.model;

import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Contains the error information.
 */
public final class ApiError {
    private final HttpStatus status;
    private final String message;
    private final List<String> errors;

    /**
     * @param status The {@link HttpStatus} corresponding to the failure.
     * @param message The generic error message that gets displayed.
     * @param errors The List of errors that indicates the actual values which failed validation.
     */
    public ApiError(HttpStatus status, String message, List<String> errors) {
        this.status = status;
        this.message = message;
        this.errors = errors;
    }

    /**
     * @param status The {@link HttpStatus} corresponding to the failure.
     * @param message The generic error message that gets displayed.
     * @param error The error that indicates the actual value which failed validation.
     */
    public ApiError(HttpStatus status, String message, String error) {
        this.status = status;
        this.message = message;
        errors = Arrays.asList(error);
    }

    /**
     * @return The {@link HttpStatus} corresponding to the failure.
     */
    public HttpStatus getStatus() {
        return status;
    }
}