package com.liferay.sales.engineering.pulse.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.base.Objects;
import com.liferay.sales.engineering.pulse.util.StringUtils;
import org.checkerframework.common.aliasing.qual.Unique;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Entity
public class Campaign {
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime begin;
    private String description;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime end;
    @Unique
    private String externalReferenceCode;
    private @Id
    @GeneratedValue Long id;
    @Column(unique = true)
    @NotBlank
    private String name;
    @ManyToOne
    @JoinColumn(name = "status_id", nullable = false)
    private Status status;
    @NotBlank
    private String targetUrl;

    public Campaign() {
    }

    private Campaign(CampaignBuilder campaignBuilder) {
        this.name = campaignBuilder.name;
        this.description = campaignBuilder.description;
        this.begin = campaignBuilder.begin;
        this.end = campaignBuilder.end;
        this.targetUrl = campaignBuilder.campaignUrl;
        this.status = campaignBuilder.status;
        this.externalReferenceCode = campaignBuilder.erc;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof Campaign)) return false;
        final Campaign campaign = (Campaign) o;
        return Objects.equal(externalReferenceCode, campaign.externalReferenceCode) && Objects.equal(getBegin(), campaign.getBegin()) && Objects.equal(getTargetUrl(), campaign.getTargetUrl()) && Objects.equal(getDescription(), campaign.getDescription()) && Objects.equal(getEnd(), campaign.getEnd()) && Objects.equal(getId(), campaign.getId()) && Objects.equal(getName(), campaign.getName()) && Objects.equal(getStatus(), campaign.getStatus());
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
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

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(final LocalDateTime end) {
        this.end = end;
    }

    public String getExternalReferenceCode() {
        return externalReferenceCode;
    }

    public void setExternalReferenceCode(final String externalReferenceCode) {
        this.externalReferenceCode = externalReferenceCode;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(final Status status) {
        this.status = status;
    }

    public String getTargetUrl() {
        return targetUrl;
    }

    public void setTargetUrl(final String campaignUrl) {
        this.targetUrl = campaignUrl;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(externalReferenceCode, getBegin(), getTargetUrl(), getDescription(), getEnd(), getId(), getName(), getStatus());
    }

    @Override
    public String toString() {
        return "Campaign{" +
                "externalReferenceCode='" + externalReferenceCode + '\'' +
                ", begin=" + begin +
                ", campaignUrl='" + targetUrl + '\'' +
                ", description='" + description + '\'' +
                ", end=" + end +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", status=" + status +
                '}';
    }

    public static class CampaignBuilder {
        LocalDateTime begin;
        String campaignUrl;
        String description;
        LocalDateTime end;
        String erc;
        String name;
        Status status;

        public CampaignBuilder(String erc, String name, Status status, String campaignUrl) {
            if (StringUtils.isBlank(erc)) {
                throw new IllegalArgumentException("External reference code must have a value");
            }
            if (StringUtils.isBlank(name)) {
                throw new IllegalArgumentException("Name must have a value");
            }
            if (status == null) {
                throw new IllegalArgumentException("Status cannot be null");
            }
            if (StringUtils.isBlank(campaignUrl)) {
                throw new IllegalArgumentException("CampaignUrl must have a value");
            }
            this.erc = erc;
            this.name = name;
            this.status = status;
            this.campaignUrl = campaignUrl;
            this.begin = LocalDateTime.now(ZoneId.of("UTC"));
        }

        public Campaign build() {
            return new Campaign(this);
        }

        public CampaignBuilder withBegin(LocalDateTime begin) {
            if (begin == null) {
                throw new IllegalArgumentException("Begin cannot be null");
            }
            this.begin = begin;
            return this;
        }

        public CampaignBuilder withDescription(String description) {
            this.description = description;
            return this;
        }

        public CampaignBuilder withEnd(LocalDateTime end) {
            this.end = end;
            return this;
        }
    }
}