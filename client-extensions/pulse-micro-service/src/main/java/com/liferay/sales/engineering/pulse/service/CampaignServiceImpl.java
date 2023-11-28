package com.liferay.sales.engineering.pulse.service;

import com.liferay.sales.engineering.pulse.DuplicateCampaignNameException;
import com.liferay.sales.engineering.pulse.model.Campaign;
import com.liferay.sales.engineering.pulse.model.Status;
import com.liferay.sales.engineering.pulse.persistence.CampaignRepository;
import com.liferay.sales.engineering.pulse.persistence.StatusRepository;
import com.liferay.sales.engineering.pulse.service.liferay.LiferayCampaignService;
import com.liferay.sales.engineering.pulse.util.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import java.net.URISyntaxException;

@Service
public class CampaignServiceImpl implements CampaignService {
    private static final Log _log = LogFactory.getLog(
            CampaignServiceImpl.class);
    private final CampaignRepository campaignRepository;
    private final LiferayCampaignService liferayCampaignService;
    private final StatusRepository statusRepository;

    public CampaignServiceImpl(final CampaignRepository campaignRepository, final StatusRepository statusRepository, final LiferayCampaignService liferayCampaignService) {
        this.campaignRepository = campaignRepository;
        this.statusRepository = statusRepository;
        this.liferayCampaignService = liferayCampaignService;
    }

    private Campaign addCampaign(com.liferay.sales.engineering.pulse.service.liferay.model.Campaign campaign) {
        return addCampaign(campaign.getExternalReferenceCode(), campaign.getName(), campaign.getTargetUrl(), campaign.getCampaignStatus().getKey());
    }

    @Override
    public Campaign addCampaign(final String erc, final String name, final String targetUrl, final String status) {
        final Status statusObj = getStatus(status);

        Campaign.CampaignBuilder campaignBuilder = new Campaign.CampaignBuilder(erc, name, statusObj, targetUrl);
        final Campaign campaign = campaignBuilder.build();

        if (campaign != null) {
            _log.debug("Saving campaign");
            campaignRepository.save(campaign);
        }

        return campaign;
    }

    @Override
    public Campaign createCampaign(final String name, final String targetUrl, final String status) throws URISyntaxException {
        if (existsByName(name)) {
            throw new DuplicateCampaignNameException(name);
        }
        final com.liferay.sales.engineering.pulse.service.liferay.model.Campaign campaign = liferayCampaignService.createCampaign(name, targetUrl, status);
        return addCampaign(campaign);
    }

    @Override
    public boolean existsByName(final String name) {
        return campaignRepository.existsByName(name);
    }

    private Status getStatus(String value) {
        final String name = StringUtils.convertToTitleCaseIteratingChars(value);
        return statusRepository.findByName(name);
    }
}
