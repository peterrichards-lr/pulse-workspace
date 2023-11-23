package com.liferay.sales.engineering.pulse.service.liferay;

import com.liferay.sales.engineering.pulse.service.liferay.model.Campaign;

import java.net.URISyntaxException;
import java.util.List;

public interface LiferayCampaignService {
    List<Campaign> getCampaigns() throws URISyntaxException;
    Campaign getByErc(String erc) throws URISyntaxException;
}
