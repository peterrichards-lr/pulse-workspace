package com.liferay.sales.engineering.pulse.rest.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class CampaignDto {
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime begin = LocalDateTime.now(ZoneId.of("UTC"));
    @NotBlank(message = "The campaign must have a target URL")
    private String campaignUrl;
    private String description;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime end;
    @NotBlank(message = "The campaign must have a name")
    private String name;
    private String status = "draft";
    private String utmContent;
    private String utmMedium;
    private String utmSource;
    private String utmTerm;

    public LocalDateTime getBegin() {
        return begin;
    }

    public void setBegin(final LocalDateTime begin) {
        this.begin = begin;
    }

    public String getCampaignUrl() {
        return campaignUrl;
    }

    public void setCampaignUrl(final String campaignUrl) {
        this.campaignUrl = campaignUrl;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(final String status) {
        this.status = status;
    }

    public String getUtmContent() {
        return utmContent;
    }

    public void setUtmContent(final String utmContent) {
        this.utmContent = utmContent;
    }

    public String getUtmMedium() {
        return utmMedium;
    }

    public void setUtmMedium(final String utmMedium) {
        this.utmMedium = utmMedium;
    }

    public String getUtmSource() {
        return utmSource;
    }

    public void setUtmSource(final String utmSource) {
        this.utmSource = utmSource;
    }

    public String getUtmTerm() {
        return utmTerm;
    }

    public void setUtmTerm(final String utmTerm) {
        this.utmTerm = utmTerm;
    }

    @Override
    public String toString() {
        return "CampaignDto{" +
                "begin=" + begin +
                ", end=" + end +
                ", name='" + name + '\'' +
                ", campaignUrl='" + campaignUrl + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ", utmMedium='" + utmMedium + '\'' +
                ", utmSource='" + utmSource + '\'' +
                ", utmTerm='" + utmTerm + '\'' +
                ", utmContent='" + utmContent + '\'' +
                '}';
    }
}