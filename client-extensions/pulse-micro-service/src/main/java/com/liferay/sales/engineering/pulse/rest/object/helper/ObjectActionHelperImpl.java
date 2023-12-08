package com.liferay.sales.engineering.pulse.rest.object.helper;

import com.liferay.sales.engineering.pulse.model.Acquisition;
import com.liferay.sales.engineering.pulse.model.Campaign;
import com.liferay.sales.engineering.pulse.rest.object.AcquisitionObjectActionController;
import com.liferay.sales.engineering.pulse.rest.object.model.ObjectAction;
import com.liferay.sales.engineering.pulse.service.AcquisitionService;
import com.liferay.sales.engineering.pulse.service.CampaignService;
import com.liferay.sales.engineering.pulse.service.UrlTokenService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class ObjectActionHelperImpl implements ObjectActionHelper {
    private static final Log _log = LogFactory.getLog(
            ObjectActionHelperImpl.class);
    private final AcquisitionService acquisitionService;
    private final CampaignService campaignService;
    private final UrlTokenService urlTokenService;

    public ObjectActionHelperImpl(final AcquisitionService acquisitionService, final CampaignService campaignService, final UrlTokenService urlTokenService) {
        this.acquisitionService = acquisitionService;
        this.campaignService = campaignService;
        this.urlTokenService = urlTokenService;
    }

    @Override
    public void removeAcquisition(final ObjectAction objectAction) {
        acquisitionService.removeAcquisition(objectAction.getErc());
    }

    @Override
    public void removeCampaign(final ObjectAction objectAction) {
        campaignService.removeCampaign(objectAction.getErc());
    }

    @Override
    public void removeUrlToken(final ObjectAction objectAction) {
        urlTokenService.removeUrlToken(objectAction.getErc());
    }

    @Override
    public void updateAcquisition(final ObjectAction objectAction) {
        final String utmSource = objectAction.getString("source");
        final String utmMedium = objectAction.getString("medium");
        final String utmContent = objectAction.getString("content");
        final String utmTerm = objectAction.getString("term");
        acquisitionService.updateAcquisition(objectAction.getErc(), utmSource, utmMedium, utmContent, utmTerm);
    }

    @Override
    public void updateCampaign(final ObjectAction objectAction) {
        final String name = objectAction.getString("name");
        final String description = objectAction.getString("description");
        final String targetUrl = objectAction.getString("targetUrl");
        final Map<String, Object> campaignStatus = objectAction.getMap("campaignStatus");
        final String status = (String) campaignStatus.get("key");
        _log.info(String.format("status : %s", status));
        final LocalDateTime startDate = objectAction.getLocalDateTime("begin");
        final LocalDateTime endDate = objectAction.getLocalDateTime("end");
        campaignService.updateCampaign(objectAction.getErc(), name, description, targetUrl, status, startDate, endDate);

    }

    @Override
    public void updateUrlToken(final ObjectAction objectAction) {
        final String token = objectAction.getString("token");
        final String campaignErc = objectAction.getString("r_urlTokenCampaignRel_c_campaignERC");
        final String acquisitionErc = objectAction.getString("r_urlTokenAcquisitionRel_c_acquisitionERC");
        final Campaign campaign = campaignService.retrieveCampaign(campaignErc);
        final Acquisition acquisition = acquisitionService.retrieveAcquisition(acquisitionErc);
        urlTokenService.updateUrlToken(objectAction.getErc(), token, campaign, acquisition);
    }
}
