package com.liferay.sales.engineering.pulse.service;

import com.liferay.sales.engineering.pulse.DuplicateCampaignException;
import com.liferay.sales.engineering.pulse.PulseException;
import com.liferay.sales.engineering.pulse.model.Campaign;
import com.liferay.sales.engineering.pulse.model.Status;
import com.liferay.sales.engineering.pulse.persistence.CampaignRepository;
import com.liferay.sales.engineering.pulse.persistence.StatusRepository;
import com.liferay.sales.engineering.pulse.service.liferay.LiferayCampaignService;
import com.liferay.sales.engineering.pulse.util.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URISyntaxException;
import java.time.LocalDateTime;

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
        return addCampaign(erc, name, null, targetUrl, status, null, null);
    }

    @Override
    public Campaign addCampaign(final String erc, final String name, final String description, final String targetUrl, final String status, final LocalDateTime startDate, final LocalDateTime endDate) {
        final Status statusObj = getStatus(status);

        Campaign.CampaignBuilder campaignBuilder = new Campaign.CampaignBuilder(erc, name, statusObj, targetUrl);
        if (StringUtils.isNotBlank(description))
            campaignBuilder.withDescription(description);
        if (startDate != null)
            campaignBuilder = campaignBuilder.withBegin(startDate);
        if (endDate != null)
            campaignBuilder = campaignBuilder.withEnd(endDate);
        final Campaign campaign = campaignBuilder.build();

        if (campaign != null) {
            _log.debug("Saving campaign");
            campaignRepository.save(campaign);
        }

        return campaign;
    }

    @Override
    public Campaign createCampaign(final String name, final String targetUrl, final String status) throws URISyntaxException {
        return createCampaign(name, null, targetUrl, status, null, null);
    }

    @Override
    public Campaign createCampaign(final String name, final String description, final String targetUrl, final String status, final LocalDateTime startDate, final LocalDateTime endDate) throws URISyntaxException {
        if (existsByName(name)) {
            throw new DuplicateCampaignException(name);
        }
        final com.liferay.sales.engineering.pulse.service.liferay.model.Campaign campaign = liferayCampaignService.createCampaign(name, description, targetUrl, status, startDate, endDate);
        _log.debug("Liferay campaign : " + campaign);
        return addCampaign(campaign);
    }

    @Override
    public boolean existsByName(final String name) {
        return campaignRepository.existsByName(name);
    }

    @Override
    public Page<Campaign> findAll(final Pageable paging) {
        return campaignRepository.findAll(paging);
    }

    private Status getStatus(String value) {
        final String name = StringUtils.convertToTitleCaseIteratingChars(value);
        return statusRepository.findByName(name);
    }

    @Override
    public void manageCampaigns() {
        final Status expiredStatus = statusRepository.findByName("Expired");
        campaignRepository.findExpiredCampaigns().forEach((campaign -> {
            _log.debug("Expiring " + campaign.getName());
            campaign.setStatus(expiredStatus);
            campaignRepository.save(campaign);
            try {
                liferayCampaignService.updateCampaign(campaign.getExternalReferenceCode(), campaign.getName(), campaign.getDescription(), campaign.getTargetUrl(), campaign.getStatus().getName().toLowerCase(), campaign.getBegin(), campaign.getEnd());
            } catch (PulseException e) {
                throw new PulseException("Unable to expire campaign : " + campaign.getName(), e);
            }
        }));

        final Status activeStatus = statusRepository.findByName("Active");
        campaignRepository.findPendingCampaigns().forEach(campaign -> {
            _log.debug("Activating " + campaign.getName());
            campaign.setStatus(activeStatus);
            campaignRepository.save(campaign);
            try {
                liferayCampaignService.updateCampaign(campaign.getExternalReferenceCode(), campaign.getName(), campaign.getDescription(), campaign.getTargetUrl(), campaign.getStatus().getName().toLowerCase(), campaign.getBegin(), campaign.getEnd());
            } catch (PulseException e) {
                throw new PulseException("Unable to activate campaign : " + campaign.getName(), e);
            }
        });
    }

    @Override
    public void removeAll() {
        campaignRepository.deleteAll();
    }

    @Override
    @Transactional
    public void removeCampaign(final String erc) {
        if (campaignRepository.existsByExternalReferenceCode(erc)) {
            campaignRepository.deleteByExternalReferenceCode(erc);
            _log.info(String.format("Deleted campaign %s", erc));
        }
    }

    @Override
    public Campaign retrieveCampaign(final String erc) {
        return campaignRepository.findByExternalReferenceCode(erc);
    }

    @Override
    public Campaign updateCampaign(String erc, String name, String description, String targetUrl, String status, LocalDateTime startDate, LocalDateTime endDate) {
        Campaign campaign = campaignRepository.findByExternalReferenceCode(erc);
        campaign.setName(name);
        campaign.setDescription(description);
        campaign.setTargetUrl(targetUrl);
        campaign.setBegin(startDate);
        campaign.setEnd(endDate);
        Status campaignStatus = statusRepository.findByNameIgnoreCase(status);
        campaign.setStatus(campaignStatus);

        _log.trace(String.format("campaign : %s", campaign));

        campaignRepository.save(campaign);
        return campaign;
    }
}
