package com.liferay.sales.engineering.pulse.service.liferay;

import com.liferay.sales.engineering.pulse.model.UrlToken;
import com.liferay.sales.engineering.pulse.service.liferay.model.Interaction;

import java.time.LocalDateTime;

public interface LiferayCampaignInteractionService {
    Interaction createInteraction(String event, UrlToken urlToken, LocalDateTime interactionTime, String userAgent, String ipAddress);
}
