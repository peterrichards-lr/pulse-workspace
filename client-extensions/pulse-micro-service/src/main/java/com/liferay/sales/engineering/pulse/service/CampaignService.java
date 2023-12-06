package com.liferay.sales.engineering.pulse.service;

import com.liferay.sales.engineering.pulse.model.Campaign;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.net.URISyntaxException;
import java.time.LocalDateTime;

public interface CampaignService {
    Campaign addCampaign(final String erc, final String name, final String targetUrl, final String status);

    Campaign addCampaign(String erc, String name, String description, String targetUrl, String status, LocalDateTime startDate, LocalDateTime endDate);

    Campaign createCampaign(final String name, final String targetUrl, final String status) throws URISyntaxException;

    Campaign createCampaign(String name, String description, String targetUrl, String status, LocalDateTime startDate, LocalDateTime endDate) throws URISyntaxException;

    boolean existsByName(String name);

    Page<Campaign> findAll(Pageable paging);

    void removeCampaign(final String erc);

    Campaign retrieveCampaign(final String erc);

    Campaign updateCampaign(String erc, String name, String description, String targetUrl, String status, LocalDateTime startDate, LocalDateTime endDate);
}
