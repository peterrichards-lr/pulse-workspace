package com.liferay.sales.engineering.pulse.service.liferay.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;

import java.time.LocalDateTime;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Campaign extends BaseObject {
    private LocalDateTime begin;
    private String description;
    private LocalDateTime end;
    private String name;
    private String campaignStatus;
    private String targetUrl;

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof Campaign)) return false;
        if (!super.equals(o)) return false;
        final Campaign campaign = (Campaign) o;
        return Objects.equal(getBegin(), campaign.getBegin()) && Objects.equal(getDescription(), campaign.getDescription()) && Objects.equal(getEnd(), campaign.getEnd()) && Objects.equal(getName(), campaign.getName()) && Objects.equal(getCampaignStatus(), campaign.getCampaignStatus()) && Objects.equal(getTargetUrl(), campaign.getTargetUrl());
    }

    public LocalDateTime getBegin() {
        return begin;
    }

    public void setBegin(final LocalDateTime begin) {
        this.begin = begin;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(final LocalDateTime end) {
        this.end = end;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getCampaignStatus() {
        return campaignStatus;
    }

    public void setCampaignStatus(final String campaignStatus) {
        this.campaignStatus = campaignStatus;
    }

    public String getTargetUrl() {
        return targetUrl;
    }

    public void setTargetUrl(final String targetUrl) {
        this.targetUrl = targetUrl;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(super.hashCode(), getBegin(), getDescription(), getEnd(), getName(), getCampaignStatus(), getTargetUrl());
    }

    @Override
    public String toString() {
        return "Campaign{" +
                "begin=" + begin +
                ", description='" + description + '\'' +
                ", end=" + end +
                ", name='" + name + '\'' +
                ", campaignStatus='" + campaignStatus + '\'' +
                ", targetUrl='" + targetUrl + '\'' +
                "} " + super.toString();
    }

    @SuppressWarnings("unchecked")
    @JsonProperty("campaignStatus")
    private void unpackNested(Map<String,Object> campaignStatus) {
        this.campaignStatus = (String)campaignStatus.get("key");
    }
}
