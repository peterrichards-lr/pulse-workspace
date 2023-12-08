package com.liferay.sales.engineering.pulse.service;

import com.liferay.sales.engineering.pulse.CacheLoaderException;
import com.liferay.sales.engineering.pulse.service.liferay.*;
import com.liferay.sales.engineering.pulse.service.liferay.model.Acquisition;
import com.liferay.sales.engineering.pulse.service.liferay.model.Campaign;
import com.liferay.sales.engineering.pulse.service.liferay.model.UrlToken;
import com.liferay.sales.engineering.pulse.util.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.net.URISyntaxException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class CacheLoaderImpl implements CacheLoader {
    private static final Log _log = LogFactory.getLog(
            CacheLoaderImpl.class);
    private final AcquisitionService acquisitionService;
    private final int cacheRefreshDelayMs;
    private final CampaignService campaignService;
    private final LiferayAcquisitionService liferayAcquisitionService;
    private final LiferayCampaignService liferayCampaignService;
    private final LiferayUrlTokenService liferayUrlTokenService;
    private final UrlTokenService urlTokenService;

    @Autowired
    public CacheLoaderImpl(
            @Value("${pulse.cache_refresh_delay_ms}") final int cacheRefreshDelayMs,
            final AcquisitionService acquisitionService,
            final CampaignService campaignService,
            final LiferayAcquisitionService liferayAcquisitionService,
            final LiferayCampaignService liferayCampaignService,
            final LiferayUrlTokenService liferayUrlTokenService,
            final UrlTokenService urlTokenService) {
        this.cacheRefreshDelayMs = cacheRefreshDelayMs;
        this.acquisitionService = acquisitionService;
        this.campaignService = campaignService;
        this.liferayAcquisitionService = liferayAcquisitionService;
        this.liferayCampaignService = liferayCampaignService;
        this.liferayUrlTokenService = liferayUrlTokenService;
        this.urlTokenService = urlTokenService;
    }

    private com.liferay.sales.engineering.pulse.model.UrlToken addPulseUrlToken(final String erc, final String token, final com.liferay.sales.engineering.pulse.model.Campaign campaign, com.liferay.sales.engineering.pulse.model.Acquisition acquisition) {
        return urlTokenService.addUrlToken(erc, token, campaign, acquisition);
    }

    private com.liferay.sales.engineering.pulse.model.Acquisition createPulseAcquisition(final Acquisition acquisition) {
        return acquisitionService.addAcquisition(acquisition.getExternalReferenceCode(), acquisition.getSource(), acquisition.getMedium(), acquisition.getContent(), acquisition.getTerm());
    }

    private com.liferay.sales.engineering.pulse.model.Campaign createPulseCampaign(final Campaign campaign) {
        return campaignService.addCampaign(campaign.getExternalReferenceCode(), campaign.getName(), campaign.getTargetUrl(), campaign.getCampaignStatus().getKey());
    }

    @Override
    @Async("asyncTaskExecutor")
    public CompletableFuture<Void> loadCache() {
        final CompletableFuture<Void> loadCacheJob = new CompletableFuture<>();
        loadCacheJob
                .whenComplete((unused, ex) -> {
                    if (ex != null) {
                        if (ex instanceof NotFoundException) {
                            final NotFoundException notFoundException = (NotFoundException) ex;
                            _log.warn(notFoundException.getMessage());
                            return;
                        } else if (ex instanceof LiferayErrorResponseException) {
                            final LiferayErrorResponseException errorResponseException = (LiferayErrorResponseException) ex;
                            _log.info(String.format("Unable to refresh cache: %s [%s]", errorResponseException.getTitle(), errorResponseException.getStatus()));
                            return;
                        }
                        _log.error("Error when refreshing cache", ex);
                        return;
                    }
                    _log.info("Job to refresh cache completed");
                });
        try {
            _log.debug("Loading data into the cache");
            final List<UrlToken> liferayUrlTokens = retrieveUrlTokens();
            for (UrlToken liferayUrlToken : liferayUrlTokens) {
                _log.debug(String.format("Url token : %s", liferayUrlToken));
                final Campaign liferayCampaign = retrieveCampaign(liferayUrlToken.getCampaignErc());
                _log.debug(String.format("Campaign : %s", liferayCampaign));
                final Acquisition liferayAcquisition = retrieveAcquisition(liferayUrlToken.getAcquisitionErc());
                if (_log.isDebugEnabled()) {
                    if (liferayAcquisition != null)
                        _log.debug(String.format("Acquisition : %s", liferayAcquisition));
                    else
                        _log.debug("Acquisition is null");
                }

                final com.liferay.sales.engineering.pulse.model.Campaign campaign = createPulseCampaign(liferayCampaign);
                final com.liferay.sales.engineering.pulse.model.Acquisition acquisition = liferayAcquisition != null ? createPulseAcquisition(liferayAcquisition) : null;
                final com.liferay.sales.engineering.pulse.model.UrlToken urlToken = addPulseUrlToken(liferayUrlToken.getExternalReferenceCode(), liferayUrlToken.getToken(), campaign, acquisition);
                _log.debug(String.format("Created url token :%s ", urlToken));
            }
            loadCacheJob.complete(null);
            _log.debug("Loaded data into the cache");
        } catch (RuntimeException e) {
            loadCacheJob.completeExceptionally(e);
        }
        return loadCacheJob;
    }

    @Override
    @Async("asyncTaskExecutor")
    public CompletableFuture<Void> refreshCache() {
        _log.debug("Refreshing the cache");
        urlTokenService.removeAll();
        campaignService.removeAll();
        acquisitionService.removeAll();
        if (cacheRefreshDelayMs > 0) {
            try {
                Thread.sleep(cacheRefreshDelayMs);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        final CompletableFuture<Void> loadCacheJob = loadCache();
        _log.debug("Refreshed the cache");
        return loadCacheJob;
    }

    private Acquisition retrieveAcquisition(String erc) {
        try {
            if (StringUtils.isBlank(erc))
                return null;
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
}
