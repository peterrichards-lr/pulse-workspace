package com.liferay.sales.engineering.pulse.rest.api.model;

import com.google.common.base.Objects;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;

public class ApiError {

    private final List<String> errors;
    private final String message;
    private final HttpStatus status;

    public ApiError(final HttpStatus status, final String message, final List<String> errors) {
        super();
        this.status = status;
        this.message = message;
        this.errors = errors;
    }

    public ApiError(final HttpStatus status, final String message, final String error) {
        super();
        this.status = status;
        this.message = message;
        errors = Arrays.asList(error);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof ApiError)) return false;
        final ApiError apiError = (ApiError) o;
        return Objects.equal(errors, apiError.errors) && Objects.equal(message, apiError.message) && status == apiError.status;
    }

    public List<String> getErrors() {
        return errors;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(errors, message, status);
    }

    @Override
    public String toString() {
        return "ApiError{" +
                "errors=" + errors +
                ", message='" + message + '\'' +
                ", status=" + status +
                '}';
    }
}

