package com.liferay.sales.engineering.pulse;

import com.liferay.sales.engineering.pulse.model.Status;
import com.liferay.sales.engineering.pulse.persistence.StatusRepository;
import com.liferay.sales.engineering.pulse.service.AcquisitionService;
import com.liferay.sales.engineering.pulse.service.CampaignService;
import com.liferay.sales.engineering.pulse.service.UrlTokenService;
import com.liferay.sales.engineering.pulse.service.liferay.LiferayAcquisitionService;
import com.liferay.sales.engineering.pulse.service.liferay.LiferayCampaignService;
import com.liferay.sales.engineering.pulse.service.liferay.LiferayUrlTokenService;
import com.liferay.sales.engineering.pulse.service.liferay.model.Acquisition;
import com.liferay.sales.engineering.pulse.service.liferay.model.Campaign;
import com.liferay.sales.engineering.pulse.service.liferay.model.UrlToken;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.security.oauth2.client.ClientAuthorizationException;
import org.springframework.stereotype.Component;

import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

@Component
public class CacheLoader implements CommandLineRunner {
    private static final Log _log = LogFactory.getLog(
            CacheLoader.class);
    private final AcquisitionService acquisitionService;
    private final CampaignService campaignService;
    private final ConfigurableApplicationContext context;
    private final LiferayAcquisitionService liferayAcquisitionService;
    private final LiferayCampaignService liferayCampaignService;
    private final LiferayUrlTokenService liferayUrlTokenService;
    private final StatusRepository statusRepository;
    private final UrlTokenService urlTokenService;


    @Autowired
    public CacheLoader(
            final AcquisitionService acquisitionService1, final CampaignService campaignService, final ConfigurableApplicationContext context, final UrlTokenService urlTokenService, StatusRepository statusRepository, final LiferayCampaignService liferayCampaignService, final LiferayUrlTokenService liferayUrlTokenService, final LiferayAcquisitionService acquisitionService) {
        this.acquisitionService = acquisitionService1;
        this.campaignService = campaignService;
        this.context = context;
        this.urlTokenService = urlTokenService;
        this.statusRepository = statusRepository;
        this.liferayCampaignService = liferayCampaignService;
        this.liferayUrlTokenService = liferayUrlTokenService;
        this.liferayAcquisitionService = acquisitionService;
    }

    private com.liferay.sales.engineering.pulse.model.UrlToken addPulseUrlToken(String token, com.liferay.sales.engineering.pulse.model.Campaign campaign, com.liferay.sales.engineering.pulse.model.Acquisition acquisition) {
        return urlTokenService.addUrlToken(token, campaign, acquisition);
    }

    private com.liferay.sales.engineering.pulse.model.Acquisition createPulseAcquisition(Acquisition acquisition) {
        return acquisitionService.createAcquisition(acquisition.getSource(), acquisition.getMedium(), acquisition.getContent(), acquisition.getTerm());
    }

    private com.liferay.sales.engineering.pulse.model.Campaign createPulseCampaign(Campaign campaign) {
        return campaignService.createCampaign(campaign.getName(), campaign.getTargetUrl(), campaign.getCampaignStatus());
    }

    private void loadLiferayData() {
        final List<UrlToken> liferayUrlTokens = retrieveUrlTokens();
        for (UrlToken liferayUrlToken : liferayUrlTokens) {
            _log.debug(String.format("Url token : %s", liferayUrlToken));
            final Campaign liferayCampaign = retrieveCampaign(liferayUrlToken.getCampaignErc());
            _log.debug(String.format("Campaign : %s", liferayCampaign));
            final Acquisition liferayAcquisition = retrieveAcquisition(liferayUrlToken.getAcquisitionErc());
            _log.debug(String.format("Acquisition : %s", liferayAcquisition));

            final com.liferay.sales.engineering.pulse.model.Campaign campaign = createPulseCampaign(liferayCampaign);
            final com.liferay.sales.engineering.pulse.model.Acquisition acquisition = createPulseAcquisition(liferayAcquisition);
            final com.liferay.sales.engineering.pulse.model.UrlToken urlToken = addPulseUrlToken(liferayUrlToken.getToken(), campaign, acquisition);
            _log.debug(String.format("Created url token :%s ", urlToken));
        }
    }

    private void populateStatus() {
        Arrays.asList("Draft", "Active", "Complete", "Inactive", "Expired").forEach((name) -> statusRepository.save(new Status(name)));
    }

    private Acquisition retrieveAcquisition(String erc) {
        try {
            return liferayAcquisitionService.getByErc(erc);
        } catch (URISyntaxException e) {
            throw new CacheLoaderException("Unable to retrieve acquisition data", e);
        }
    }

    private Campaign retrieveCampaign(String erc) {
        try {
            return liferayCampaignService.getByErc(erc);
        } catch (URISyntaxException e) {
            throw new CacheLoaderException("Unable to retrieve campaign data", e);
        }
    }

    private List<UrlToken> retrieveUrlTokens() {
        try {
            return liferayUrlTokenService.getUrlTokens();
        } catch (URISyntaxException e) {
            throw new CacheLoaderException("Unable to retrieve url token data", e);
        }
    }

    @Override
    public void run(String... params) {
        populateStatus();
        _log.info("Populated statuses");
        try {
            loadLiferayData();
        } catch (ClientAuthorizationException e) {
            _log.error("The OAuth 2 client credentials are invalid. Unable to load cache from Liferay");
            context.close();
        } catch (CacheLoaderException e) {
            _log.error("Unable to retrieve object data", e);
            context.close();
        }
    }
}