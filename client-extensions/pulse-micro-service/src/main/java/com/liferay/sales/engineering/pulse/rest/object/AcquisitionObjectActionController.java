package com.liferay.sales.engineering.pulse.rest.object;

import com.liferay.sales.engineering.pulse.rest.BaseRestController;
import com.liferay.sales.engineering.pulse.service.AcquisitionService;
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
@RequestMapping("/object/acquisitions")
public class AcquisitionObjectActionController extends BaseRestController {
    private static final Log _log = LogFactory.getLog(
            AcquisitionObjectActionController.class);

    private final AcquisitionService acquisitionService;

    public AcquisitionObjectActionController(final AcquisitionService acquisitionService) {
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

            acquisitionService.removeAcquisition(erc);
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/update")
    public ResponseEntity<String> update(
            @AuthenticationPrincipal Jwt jwt, @RequestBody String json) {
        log(jwt, _log, json);

        final JSONObject jsonObject = new JSONObject(json);

        final JSONObject objectEntry = jsonObject.getJSONObject("objectEntryDTOAcquisition");

        final String trigger = jsonObject.getString("objectActionTriggerKey");
        _log.info("objectActionTriggerKey: " + trigger);

        if (trigger.contains("Update")) {
            final String erc = objectEntry.getString("externalReferenceCode");
            final JSONObject properties = objectEntry.getJSONObject("properties");

            if (_log.isInfoEnabled()) {
                _log.info("Properties: " + properties.toString(4));
            }

            final String utmSource = properties.getString("source");
            final String utmMedium = properties.getString("medium");
            final String utmContent = properties.getString("content");
            final String utmTerm = properties.getString("term");
            acquisitionService.updateAcquisition(erc, utmSource, utmMedium, utmContent, utmTerm);
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
