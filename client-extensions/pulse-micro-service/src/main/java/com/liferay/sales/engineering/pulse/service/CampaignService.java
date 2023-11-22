package com.liferay.sales.engineering.pulse.service;

import com.liferay.sales.engineering.pulse.model.Campaign;

public interface CampaignService {
    Campaign createCampaign(final String name, final String targetUrl, final String status);

    boolean existsByName(String name);
}
