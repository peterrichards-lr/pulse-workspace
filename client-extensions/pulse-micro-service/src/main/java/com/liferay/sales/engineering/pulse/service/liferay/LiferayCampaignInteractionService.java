package com.liferay.sales.engineering.pulse.service.liferay;

import com.liferay.sales.engineering.pulse.model.Campaign;

import java.net.URISyntaxException;
import java.time.LocalDateTime;

public interface LiferayCampaignInteractionService {
    Long createInteraction(Campaign campaign, LocalDateTime interactionTime, String userAgent, String ipAddress) throws URISyntaxException;
}
