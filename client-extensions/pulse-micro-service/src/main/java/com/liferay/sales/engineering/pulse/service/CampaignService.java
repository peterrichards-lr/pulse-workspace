package com.liferay.sales.engineering.pulse.service;

import com.liferay.sales.engineering.pulse.model.Campaign;

import java.net.URISyntaxException;

public interface CampaignService {
    Campaign addCampaign(final String erc, final String name, final String targetUrl, final String status);

    Campaign createCampaign(final String name, final String targetUrl, final String status) throws URISyntaxException;

    boolean existsByName(String name);
}
