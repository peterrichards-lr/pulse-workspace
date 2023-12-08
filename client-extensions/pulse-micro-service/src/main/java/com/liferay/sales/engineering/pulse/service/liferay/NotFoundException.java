package com.liferay.sales.engineering.pulse.service.liferay;

import com.google.common.base.Objects;

import java.net.URI;

public class NotFoundException extends RuntimeException {
    private final URI endpoint;

    public NotFoundException(final URI endpoint) {
        super(String.format("Not found -  %s", endpoint));
        this.endpoint = endpoint;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof NotFoundException)) return false;
        final NotFoundException that = (NotFoundException) o;
        return Objects.equal(endpoint, that.endpoint);
    }

    public URI getEndpoint() {
        return endpoint;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(endpoint);
    }

    @Override
    public String toString() {
        return "NotFoundException{" +
                "endpoint=" + endpoint +
                "} " + super.toString();
    }
}
