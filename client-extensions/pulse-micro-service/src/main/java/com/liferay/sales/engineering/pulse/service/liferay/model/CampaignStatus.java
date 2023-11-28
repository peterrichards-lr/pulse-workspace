package com.liferay.sales.engineering.pulse.service.liferay.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.base.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CampaignStatus {
    private String key;
    private String name;

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof CampaignStatus)) return false;
        final CampaignStatus that = (CampaignStatus) o;
        return Objects.equal(getKey(), that.getKey()) && Objects.equal(getName(), that.getName());
    }

    public String getKey() {
        return key;
    }

    public void setKey(final String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getKey(), getName());
    }

    @Override
    public String toString() {
        return "CampaignStatus{" +
                "key='" + key + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
