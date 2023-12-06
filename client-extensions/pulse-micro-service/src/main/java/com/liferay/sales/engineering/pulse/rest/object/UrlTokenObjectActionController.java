package com.liferay.sales.engineering.pulse.rest.object;

import com.liferay.sales.engineering.pulse.model.Acquisition;
import com.liferay.sales.engineering.pulse.model.Campaign;
import com.liferay.sales.engineering.pulse.rest.BaseRestController;
import com.liferay.sales.engineering.pulse.service.AcquisitionService;
import com.liferay.sales.engineering.pulse.service.CampaignService;
import com.liferay.sales.engineering.pulse.service.UrlTokenService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/object/url-tokens")
public class UrlTokenObjectActionController extends BaseRestController {
    private static final Log _log = LogFactory.getLog(
            UrlTokenObjectActionController.class);
    private final AcquisitionService acquisitionService;
    private final CampaignService campaignService;
    private final UrlTokenService urlTokenService;

    public UrlTokenObjectActionController(final UrlTokenService urlTokenService, final CampaignService campaignService, final AcquisitionService acquisitionService) {
        this.urlTokenService = urlTokenService;
        this.campaignService = campaignService;
        this.acquisitionService = acquisitionService;
    }

    @PostMapping("/delete")
    public ResponseEntity<String> delete(
            @AuthenticationPrincipal Jwt jwt, @RequestBody String json) {
        log(jwt, _log, json);

        final JSONObject jsonObject = new JSONObject(json);

        final JSONObject objectEntry = jsonObject.getJSONObject("objectEntryDTOUrlToken");

        final String trigger = jsonObject.getString("objectActionTriggerKey");
        _log.info("objectActionTriggerKey: " + trigger);

        if (trigger.contains("Delete")) {
            final String erc = objectEntry.getString("externalReferenceCode");
            final JSONObject properties = objectEntry.getJSONObject("properties");

            if (_log.isInfoEnabled()) {
                _log.info("Properties: " + properties.toString(4));
            }

            urlTokenService.removeUrlToken(erc);
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/update")
    public ResponseEntity<String> update(
            @AuthenticationPrincipal Jwt jwt, @RequestBody String json) {
        log(jwt, _log, json);

        final JSONObject jsonObject = new JSONObject(json);

        final JSONObject objectEntry = jsonObject.getJSONObject("objectEntryDTOUrlToken");

        final String trigger = jsonObject.getString("objectActionTriggerKey");
        _log.info("objectActionTriggerKey: " + trigger);

        if (trigger.contains("Update")) {
            final String erc = objectEntry.getString("externalReferenceCode");
            final JSONObject properties = objectEntry.getJSONObject("properties");

            if (_log.isInfoEnabled()) {
                _log.info("Properties: " + properties.toString(4));
            }

            final String token = properties.getString("token");
            final String campaignErc = properties.getString("r_urlTokenCampaignRel_c_campaignERC");
            final String acquisitionErc = properties.getString("r_urlTokenAcquisitionRel_c_acquisitionERC");

            final Campaign campaign = campaignService.retrieveCampaign(campaignErc);
            final Acquisition acquisition = acquisitionService.retrieveAcquisition(acquisitionErc);

            urlTokenService.updateUrlToken(erc, token, campaign, acquisition);
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
