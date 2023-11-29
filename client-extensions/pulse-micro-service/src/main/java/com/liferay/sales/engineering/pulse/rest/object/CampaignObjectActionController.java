package com.liferay.sales.engineering.pulse.rest.object;

import com.liferay.sales.engineering.pulse.rest.BaseRestController;
import com.liferay.sales.engineering.pulse.service.CampaignService;
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
@RequestMapping("/object/campaigns")
public class CampaignObjectActionController extends BaseRestController {
    private static final Log _log = LogFactory.getLog(
            CampaignObjectActionController.class);

    private final CampaignService campaignService;

    public CampaignObjectActionController(final CampaignService campaignService) {
        this.campaignService = campaignService;
    }

    @PostMapping
    public ResponseEntity<String> post(
            @AuthenticationPrincipal Jwt jwt, @RequestBody String json) {
        log(jwt, _log, json);

        final JSONObject jsonObject = new JSONObject(json);

        final JSONObject objectEntry = jsonObject.getJSONObject("objectEntryDTOCampaign");

        final String trigger = jsonObject.getString("objectActionTriggerKey");
        _log.info("objectActionTriggerKey: " + trigger);

        if (trigger.contains("Delete")) {
            final String erc = objectEntry.getString("externalReferenceCode");
            final JSONObject properties = objectEntry.getJSONObject("properties");

            if (_log.isInfoEnabled()) {
                _log.info("Properties: " + properties.toString(4));
            }

            campaignService.removeCampaign(erc);
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
