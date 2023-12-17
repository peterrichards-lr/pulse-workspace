package com.liferay.sales.engineering.pulse;

import com.liferay.sales.engineering.pulse.service.CampaignService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CampaignManager {
    private static final Log _log = LogFactory.getLog(
            CampaignManager.class);
    private final CampaignService campaignService;

    public CampaignManager(final CampaignService campaignService) {
        this.campaignService = campaignService;
    }

    @Scheduled(cron = "${pulse.cron_schedule}")
    public void task() {
        _log.info("Check for expired campaigns");
        campaignService.manageCampaigns();
    }
}
