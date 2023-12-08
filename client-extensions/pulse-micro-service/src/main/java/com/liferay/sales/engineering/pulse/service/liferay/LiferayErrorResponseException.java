package com.liferay.sales.engineering.pulse.service.liferay;

import com.google.common.base.Objects;
import com.liferay.sales.engineering.pulse.service.liferay.model.LiferayErrorResponse;
import org.springframework.http.HttpStatus;

public class LiferayErrorResponseException extends RuntimeException {
    private final LiferayErrorResponse errorResponse;

    public LiferayErrorResponseException(LiferayErrorResponse errorResponse) {
        super(errorResponse != null ? errorResponse.getTitle() : null);
        this.errorResponse = errorResponse;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof LiferayErrorResponseException)) return false;
        final LiferayErrorResponseException that = (LiferayErrorResponseException) o;
        return Objects.equal(errorResponse, that.errorResponse);
    }


    public HttpStatus getStatus() {
        return errorResponse != null ? errorResponse.getStatus() : null;
    }

    public String getTitle() {
        return errorResponse != null ? errorResponse.getTitle() : null;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(errorResponse);
    }

    @Override
    public String toString() {
        return "LiferayErrorResponseException{" +
                ", errorResponse=" + errorResponse +
                "} " + super.toString();
    }
}
