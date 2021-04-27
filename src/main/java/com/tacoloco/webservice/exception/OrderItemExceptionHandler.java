package com.tacoloco.webservice.exception;

import com.tacoloco.webservice.model.ApiError;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

/**
 * Exception handler for {@link com.tacoloco.webservice.controller.OrderItemController}.
 */
@ControllerAdvice
public final class OrderItemExceptionHandler {
        /**
         * Handles constraints associated to {@link ConstraintViolationException}.
         * @param constraintViolationException The {@link ConstraintViolationException} that is thrown by the controller.
         * @param request The {@link WebRequest} that is passed to the rest controller.
         * @return The error {@link ResponseEntity} for the constraint exception.
         */
    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<Object> handleConstraintViolation(
            ConstraintViolationException constraintViolationException, WebRequest request) {
        List<String> errors = new ArrayList<String>();
        for (ConstraintViolation<?> violation : constraintViolationException.getConstraintViolations()) {
            errors.add(violation.getPropertyPath() + " " + violation.getInvalidValue() + ": " + violation.getMessage());
        }

        ApiError apiError =
                new ApiError(HttpStatus.BAD_REQUEST, "The request is not valid", errors);
        return new ResponseEntity<Object>(
                apiError, new HttpHeaders(), apiError.getStatus());

    }
}