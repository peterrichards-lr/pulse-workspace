package com.liferay.sales.engineering.pulse.service.liferay.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;
import com.liferay.sales.engineering.pulse.model.Campaign;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Interaction extends BaseObject {

    @JsonProperty("r_campaignInteractionRel_c_campaignERC")
    private String campaignErc;
    private String event;
    private String eventProperties;
    private long id;

    public Interaction() {

    }

    public Interaction(Campaign campaign) {
        this.campaignErc = campaign.getExternalReferenceCode();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof Interaction)) return false;
        if (!super.equals(o)) return false;
        final Interaction that = (Interaction) o;
        return getId() == that.getId() && Objects.equal(getCampaignErc(), that.getCampaignErc()) && Objects.equal(getEvent(), that.getEvent()) && Objects.equal(getEventProperties(), that.getEventProperties());
    }

    public String getCampaignErc() {
        return campaignErc;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(final String event) {
        this.event = event;
    }

    public String getEventProperties() {
        return eventProperties;
    }

    public void setEventProperties(final String eventProperties) {
        this.eventProperties = eventProperties;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(super.hashCode(), getId(), getCampaignErc(), getEvent(), getEventProperties());
    }

    @Override
    public String toString() {
        return "Interaction{" +
                "id=" + id +
                ", campaignErc='" + campaignErc + '\'' +
                ", event='" + event + '\'' +
                ", eventProperties='" + eventProperties + '\'' +
                "} " + super.toString();
    }
}
