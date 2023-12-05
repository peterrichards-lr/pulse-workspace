package com.liferay.sales.engineering.pulse.rest.api.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.time.ZoneId;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CampaignDto {
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime begin = LocalDateTime.now(ZoneId.of("UTC"));
    private String campaignStatus = "draft";
    private String description;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime end;
    @NotBlank(message = "The campaign must have a name")
    private String name;
    @NotBlank(message = "The campaign must have a target URL")
    private String targetUrl;
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

    public String getCampaignStatus() {
        return campaignStatus;
    }

    public void setCampaignStatus(final String campaignStatus) {
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
                ", targetUrl='" + targetUrl + '\'' +
                ", description='" + description + '\'' +
                ", campaignStatus='" + campaignStatus + '\'' +
                ", utmMedium='" + utmMedium + '\'' +
                ", utmSource='" + utmSource + '\'' +
                ", utmTerm='" + utmTerm + '\'' +
                ", utmContent='" + utmContent + '\'' +
                '}';
    }
}