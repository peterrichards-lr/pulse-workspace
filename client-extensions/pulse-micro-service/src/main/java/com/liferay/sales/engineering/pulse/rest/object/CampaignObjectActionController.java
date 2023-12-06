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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/object/campaigns")
public class CampaignObjectActionController extends BaseRestController {
    private static final Log _log = LogFactory.getLog(
            CampaignObjectActionController.class);

    private final CampaignService campaignService;

    public CampaignObjectActionController(final CampaignService campaignService) {
        this.campaignService = campaignService;
    }

    @PostMapping("/delete")
    public ResponseEntity<String> delete(
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

    @PostMapping("/update")
    public ResponseEntity<String> update(
            @AuthenticationPrincipal Jwt jwt, @RequestBody String json) {
        log(jwt, _log, json);

        final JSONObject jsonObject = new JSONObject(json);

        final JSONObject objectEntry = jsonObject.getJSONObject("objectEntryDTOCampaign");

        final String trigger = jsonObject.getString("objectActionTriggerKey");
        _log.info("objectActionTriggerKey: " + trigger);

        if (trigger.contains("Update")) {
            final String erc = objectEntry.getString("externalReferenceCode");
            final JSONObject properties = objectEntry.getJSONObject("properties");

            if (_log.isInfoEnabled()) {
                _log.info("Properties: " + properties.toString(4));
            }

            final String name = properties.getString("name");
            final String description = properties.getString("description");
            final String targetUrl = properties.getString("targetUrl");
            final JSONObject campaignStatus = properties.getJSONObject("campaignStatus");
            final String status = campaignStatus.getString("key");

            final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            final LocalDateTime startDate = properties.has("begin") ? LocalDateTime.parse(properties.getString("begin"), dateTimeFormatter) : null;
            final LocalDateTime endDate = properties.has("end") ? LocalDateTime.parse(properties.getString("end"), dateTimeFormatter) : null;

            campaignService.updateCampaign(erc, name, description, targetUrl, status, startDate, endDate);
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
