package com.liferay.sales.engineering.pulse.rest;

import com.liferay.sales.engineering.pulse.DuplicateCampaignNameException;
import com.liferay.sales.engineering.pulse.rest.api.model.ApiError;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class CustomRestExceptionHandler extends ResponseEntityExceptionHandler {
    private static final Log _log = LogFactory.getLog(
            CustomRestExceptionHandler.class);

    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<Object> handleConstraintViolation(
            final ConstraintViolationException ex, final WebRequest request) {
        final List<String> errors = new ArrayList<>();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            errors.add(violation.getRootBeanClass().getName() + " " +
                    violation.getPropertyPath() + ": " + violation.getMessage());
        }

        final ApiError apiError =
                new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), errors);
        _log.warn(apiError);
        return new ResponseEntity<>(
                apiError, new HttpHeaders(), apiError.getStatus());
    }

    @ExceptionHandler({DuplicateCampaignNameException.class})
    public ResponseEntity<Object> handleDuplicateCampaignNameException(
            final DuplicateCampaignNameException ex, final WebRequest request) {
        final String error =
                ex.getMessage();

        final ApiError apiError =
                new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), error);
        _log.warn(apiError);
        return new ResponseEntity<>(
                apiError, new HttpHeaders(), apiError.getStatus());
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public ResponseEntity<Object> handleMethodArgumentTypeMismatch(
            final MethodArgumentTypeMismatchException ex, final WebRequest request) {
        final String error =
                ex.getName() + " should be of type " + ex.getRequiredType().getName();

        final ApiError apiError =
                new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), error);
        _log.warn(apiError);
        return new ResponseEntity<>(
                apiError, new HttpHeaders(), apiError.getStatus());
    }
}