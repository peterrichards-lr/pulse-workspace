package com.liferay.sales.engineering.pulse.service.liferay.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.base.Objects;

import java.time.LocalDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Campaign extends BaseObject {
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private LocalDateTime begin;
    private CampaignStatus campaignStatus;
    private String description;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private LocalDateTime end;
    private String name;
    private String targetUrl;

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof Campaign)) return false;
        if (!super.equals(o)) return false;
        final Campaign campaign = (Campaign) o;
        return Objects.equal(getBegin(), campaign.getBegin()) && Objects.equal(getCampaignStatus(), campaign.getCampaignStatus()) && Objects.equal(getDescription(), campaign.getDescription()) && Objects.equal(getEnd(), campaign.getEnd()) && Objects.equal(getName(), campaign.getName()) && Objects.equal(getTargetUrl(), campaign.getTargetUrl());
    }

    public LocalDateTime getBegin() {
        return begin;
    }

    public void setBegin(final LocalDateTime begin) {
        this.begin = begin;
    }

    public CampaignStatus getCampaignStatus() {
        return campaignStatus;
    }

    public void setCampaignStatus(final CampaignStatus campaignStatus) {
        this.campaignStatus = campaignStatus;
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

    public String getTargetUrl() {
        return targetUrl;
    }

    public void setTargetUrl(final String targetUrl) {
        this.targetUrl = targetUrl;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(super.hashCode(), getBegin(), getCampaignStatus(), getDescription(), getEnd(), getName(), getTargetUrl());
    }

    @Override
    public String toString() {
        return "Campaign{" +
                "begin=" + begin +
                ", campaignStatus=" + campaignStatus +
                ", description='" + description + '\'' +
                ", end=" + end +
                ", name='" + name + '\'' +
                ", targetUrl='" + targetUrl + '\'' +
                "} " + super.toString();
    }
}
