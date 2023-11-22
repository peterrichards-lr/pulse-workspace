package com.liferay.sales.engineering.pulse;

import com.liferay.sales.engineering.pulse.model.Status;
import com.liferay.sales.engineering.pulse.persistence.StatusRepository;
import com.liferay.sales.engineering.pulse.service.liferay.LiferayAcquisitionService;
import com.liferay.sales.engineering.pulse.service.liferay.LiferayCampaignService;
import com.liferay.sales.engineering.pulse.service.liferay.LiferayUrlTokenService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.security.oauth2.client.ClientAuthorizationException;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class CacheLoader implements CommandLineRunner {
    private static final Log _log = LogFactory.getLog(
            CacheLoader.class);
    private final LiferayAcquisitionService acquisitionService;
    private final ConfigurableApplicationContext context;
    private final LiferayCampaignService liferayCampaignService;
    private final LiferayUrlTokenService liferayUrlTokenService;
    private final StatusRepository statusRepository;

    @Autowired
    public CacheLoader(
            final ConfigurableApplicationContext context, StatusRepository statusRepository, final LiferayCampaignService liferayCampaignService, final LiferayUrlTokenService liferayUrlTokenService, final LiferayAcquisitionService acquisitionService) {
        this.context = context;
        this.statusRepository = statusRepository;
        this.liferayCampaignService = liferayCampaignService;
        this.liferayUrlTokenService = liferayUrlTokenService;
        this.acquisitionService = acquisitionService;
    }

    private void populateStatus() {
        Arrays.asList("Draft", "Active", "Complete", "Inactive", "Expired").forEach((name) -> statusRepository.save(new Status(name)));
    }

    private void retrieveAcquisitions() {
        List<Object> acquisitions = acquisitionService.getAcquisitions();

        if (acquisitions == null)
            return;

        _log.info("***** ACQUISITIONS *****");
        for (Object acquisision : acquisitions) {
            _log.info(acquisision);
        }
        _log.info("*********************");
    }

    private void retrieveCampaigns() {
        List<Object> campaigns = liferayCampaignService.getCampaigns();

        if (campaigns == null)
            return;

        _log.info("***** CAMPAIGNS *****");
        for (Object campaign : campaigns) {
            _log.info(campaign);
        }
        _log.info("*********************");
    }

    private void retrieveUrlTokens() {
        List<Object> urlTokens = liferayUrlTokenService.getUrlTokens();

        if (urlTokens == null)
            return;

        _log.info("***** URL TOKENS *****");
        for (Object urlToken : urlTokens) {
            _log.info(urlToken);
        }
        _log.info("*********************");
    }

    @Override
    public void run(String... params) {
        populateStatus();
        _log.info("Populated statuses");
        try {
            retrieveUrlTokens();
            retrieveCampaigns();
            retrieveAcquisitions();
        } catch (ClientAuthorizationException e) {
            _log.error("The OAuth 2 client credentials are invalid. Unable to load cache from Liferay");
            context.close();
        }
    }
}