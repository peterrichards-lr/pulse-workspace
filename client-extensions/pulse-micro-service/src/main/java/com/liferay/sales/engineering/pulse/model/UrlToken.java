package com.liferay.sales.engineering.pulse.model;

import com.google.common.base.Objects;
import com.liferay.sales.engineering.pulse.util.StringUtils;
import org.checkerframework.common.aliasing.qual.Unique;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.validation.Valid;

@Entity
public class UrlToken {
    @OneToOne
    @JoinColumn(name = "acquisition_id")
    private Acquisition acquisition;
    @OneToOne
    @JoinColumn(name = "campaign_id", nullable = false)
    @Valid
    private Campaign campaign;
    @Unique
    private String externalReferenceCode;
    private @Id String token;

    public UrlToken() {
    }

    private UrlToken(UrlTokenBuilder builder) {
        this.token = builder.token;
        this.campaign = builder.campaign;
        this.acquisition = builder.acquisition;
        this.externalReferenceCode = builder.erc;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof UrlToken)) return false;
        final UrlToken urlToken = (UrlToken) o;
        return Objects.equal(getAcquisition(), urlToken.getAcquisition()) && Objects.equal(getCampaign(), urlToken.getCampaign()) && Objects.equal(getToken(), urlToken.getToken()) && Objects.equal(externalReferenceCode, urlToken.externalReferenceCode);
    }

    public Acquisition getAcquisition() {
        return acquisition;
    }

    public void setAcquisition(Acquisition acquisition) {
        this.acquisition = acquisition;
    }

    public Campaign getCampaign() {
        return campaign;
    }

    public void setCampaign(Campaign campaign) {
        this.campaign = campaign;
    }

    public String getExternalReferenceCode() {
        return externalReferenceCode;
    }

    public void setExternalReferenceCode(final String externalReferenceCode) {
        this.externalReferenceCode = externalReferenceCode;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getAcquisition(), getCampaign(), getToken(), externalReferenceCode);
    }

    @Override
    public String toString() {
        return "UrlToken{" +
                "acquisition=" + acquisition +
                ", campaign=" + campaign +
                ", token='" + token + '\'' +
                ", externalReferenceCode='" + externalReferenceCode + '\'' +
                '}';
    }

    public static class UrlTokenBuilder {
        private final Campaign campaign;
        private final String token;
        private Acquisition acquisition;
        private String erc;

        public UrlTokenBuilder(String token, Campaign campaign) {
            if (StringUtils.isBlank(token)) {
                throw new IllegalArgumentException("Token must have a value");
            }
            if (campaign == null) {
                throw new IllegalArgumentException("Campaign cannot be null");
            }
            this.token = token;
            this.campaign = campaign;
        }

        public UrlToken build() {
            return new UrlToken(this);
        }

        public UrlTokenBuilder withAcquisition(Acquisition acquisition) {
            if (acquisition == null) {
                throw new IllegalArgumentException("Acquisition cannot be null");
            }
            this.acquisition = acquisition;
            return this;
        }

        public UrlTokenBuilder withExternalReferenceCode(String erc) {
            if (erc == null) {
                throw new IllegalArgumentException("External reference code cannot be null");
            }
            this.erc = erc;
            return this;
        }
    }
}
