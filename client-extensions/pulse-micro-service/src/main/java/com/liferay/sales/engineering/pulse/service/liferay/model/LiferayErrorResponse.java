package com.liferay.sales.engineering.pulse.service.liferay.model;

import com.google.common.base.Objects;
import org.springframework.http.HttpStatus;

public class LiferayErrorResponse {

    private HttpStatus status;
    private String title;

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof LiferayErrorResponse)) return false;
        final LiferayErrorResponse that = (LiferayErrorResponse) o;
        return Objects.equal(getStatus(), that.getStatus()) && Objects.equal(getTitle(), that.getTitle());
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(final String status) {
        this.status = HttpStatus.valueOf(status);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getStatus(), getTitle());
    }

    @Override
    public String toString() {
        return "LiferayErrorResponse{" +
                "status='" + status + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}