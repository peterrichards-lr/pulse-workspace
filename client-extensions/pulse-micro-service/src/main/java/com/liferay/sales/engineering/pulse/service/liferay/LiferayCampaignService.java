package com.liferay.sales.engineering.pulse.service.liferay;

import com.liferay.sales.engineering.pulse.service.liferay.model.Campaign;

import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.List;

public interface LiferayCampaignService {
    Campaign createCampaign(final String name, final String targetUrl, final String status) throws URISyntaxException;

    Campaign createCampaign(final String name, final String description, final String targetUrl, final String status, final LocalDateTime startDate, final LocalDateTime endDate) throws URISyntaxException;

    Campaign getByErc(String erc) throws URISyntaxException;

    List<Campaign> getCampaigns() throws URISyntaxException;
}
