package com.liferay.sales.engineering.pulse.service.liferay;

import com.liferay.sales.engineering.pulse.model.UrlToken;
import com.liferay.sales.engineering.pulse.service.liferay.model.Interaction;

import java.net.URISyntaxException;
import java.time.LocalDateTime;

public interface LiferayCampaignInteractionService {
    Interaction createInteraction(UrlToken urlToken, LocalDateTime interactionTime, String userAgent, String ipAddress) throws URISyntaxException;
}
