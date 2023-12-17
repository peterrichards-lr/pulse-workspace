package com.liferay.sales.engineering.pulse.service.liferay;

import com.liferay.sales.engineering.pulse.service.liferay.model.Campaign;

import java.time.LocalDateTime;
import java.util.List;

public interface LiferayCampaignService {
    Campaign createCampaign(final String name, final String targetUrl, final String status);

    Campaign createCampaign(final String name, final String description, final String targetUrl, final String status, final LocalDateTime startDate, final LocalDateTime endDate);

    Campaign getByErc(String erc);

    List<Campaign> getCampaigns();

    Campaign updateCampaign(final String erc, final String name, final String description, final String targetUrl, final String status, final LocalDateTime startDate, final LocalDateTime endDate);
}
