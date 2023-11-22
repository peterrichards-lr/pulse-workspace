package com.liferay.sales.engineering.pulse.service.liferay.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.base.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UrlToken extends BaseObject {
    private String token;

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof UrlToken)) return false;
        if (!super.equals(o)) return false;
        final UrlToken urlToken = (UrlToken) o;
        return Objects.equal(getToken(), urlToken.getToken());
    }

    public String getToken() {
        return token;
    }

    public void setToken(final String token) {
        this.token = token;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(super.hashCode(), getToken());
    }

    @Override
    public String toString() {
        return "UrlToken{" +
                "token='" + token + '\'' +
                "} " + super.toString();
    }
}
