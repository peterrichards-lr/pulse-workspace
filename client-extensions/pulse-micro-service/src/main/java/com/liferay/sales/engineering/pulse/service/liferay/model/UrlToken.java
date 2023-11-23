package com.liferay.sales.engineering.pulse.service.liferay.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UrlToken extends BaseObject {
    @JsonProperty("urlTokenAcquisitionRelERC")
    private String acquisitionErc;
    @JsonProperty("urlTokenCampaignRelERC")
    private String campaignErc;
    private String token;

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof UrlToken)) return false;
        if (!super.equals(o)) return false;
        final UrlToken urlToken = (UrlToken) o;
        return Objects.equal(getToken(), urlToken.getToken()) && Objects.equal(getAcquisitionErc(), urlToken.getAcquisitionErc()) && Objects.equal(getCampaignErc(), urlToken.getCampaignErc());
    }

    public String getAcquisitionErc() {
        return acquisitionErc;
    }

    public void setAcquisitionErc(final String acquisitionErc) {
        this.acquisitionErc = acquisitionErc;
    }

    public String getCampaignErc() {
        return campaignErc;
    }

    public void setCampaignErc(final String campaignErc) {
        this.campaignErc = campaignErc;
    }

    public String getToken() {
        return token;
    }

    public void setToken(final String token) {
        this.token = token;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(super.hashCode(), getToken(), getAcquisitionErc(), getCampaignErc());
    }

    @Override
    public String toString() {
        return "UrlToken{" +
                "token='" + token + '\'' +
                ", acquisitionErc='" + acquisitionErc + '\'' +
                ", campaignErc='" + campaignErc + '\'' +
                "} " + super.toString();
    }
}
