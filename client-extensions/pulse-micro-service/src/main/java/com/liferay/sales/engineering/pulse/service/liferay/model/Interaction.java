package com.liferay.sales.engineering.pulse.service.liferay.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Interaction extends BaseObject {
    @JsonProperty("r_campaignInteractionRel_c_campaignERC")
    private String campaignErc;
    private String event;
    private String eventProperties;

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof Interaction)) return false;
        if (!super.equals(o)) return false;
        final Interaction that = (Interaction) o;
        return Objects.equal(campaignErc, that.campaignErc) && Objects.equal(event, that.event) && Objects.equal(eventProperties, that.eventProperties);
    }

    public String getCampaignErc() {
        return campaignErc;
    }

    public void setCampaignErc(final String campaignErc) {
        this.campaignErc = campaignErc;
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
    public int hashCode() {
        return Objects.hashCode(super.hashCode(), campaignErc, event, eventProperties);
    }

    @Override
    public String toString() {
        return "Interaction{" +
                "campaignErc='" + campaignErc + '\'' +
                ", event='" + event + '\'' +
                ", eventProperties='" + eventProperties + '\'' +
                "} " + super.toString();
    }
}
