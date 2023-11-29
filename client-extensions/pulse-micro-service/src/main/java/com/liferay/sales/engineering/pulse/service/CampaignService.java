package com.liferay.sales.engineering.pulse.service;

import com.liferay.sales.engineering.pulse.model.Campaign;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.net.URISyntaxException;

public interface CampaignService {
    Campaign addCampaign(final String erc, final String name, final String targetUrl, final String status);

    Campaign createCampaign(final String name, final String targetUrl, final String status) throws URISyntaxException;

    boolean existsByName(String name);

    Page<Campaign> findAll(Pageable paging);

    void removeCampaign(final String erc);
}
