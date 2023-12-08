package com.liferay.sales.engineering.pulse.rest.object;

import com.liferay.sales.engineering.pulse.rest.api.model.RefreshCacheJob;
import com.liferay.sales.engineering.pulse.rest.object.helper.ObjectActionHelper;
import com.liferay.sales.engineering.pulse.rest.object.model.ObjectAction;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/object/refresh-entry")
public class RefreshCacheEntryObjectActionController extends BaseObjectActionController {
    private static final Log _log = LogFactory.getLog(
            RefreshCacheEntryObjectActionController.class);
    private final ObjectActionHelper objectActionHelper;

    public RefreshCacheEntryObjectActionController(final ObjectActionHelper objectActionHelper) {
        this.objectActionHelper = objectActionHelper;
    }

    @PostMapping
    public ResponseEntity<RefreshCacheJob> refreshCacheEntry(
            @AuthenticationPrincipal final Jwt jwt, @RequestBody final String json) {
        log(jwt, _log, json);

        final String objectName = super.getObjectName(json, _log);
        _log.trace(String.format("objectName : %s", objectName));
        final String objectEntryPropertyName = String.format("%s%s", OBJECT_ENTRY_PROPERTY_PREFIX, objectName);
        _log.trace(String.format("objectEntryPropertyName : %s", objectEntryPropertyName));

        final ObjectAction objectAction = super.parseRequest(jwt, json, objectEntryPropertyName, _log);
        _log.debug(String.format("objectAction : %s", objectAction));

        switch (objectName) {
            case "Acquisition": {
                objectActionHelper.updateAcquisition(objectAction);
                break;
            }
            case "Campaign": {
                objectActionHelper.updateCampaign(objectAction);
                break;
            }
            case "UrlToken": {
                objectActionHelper.updateUrlToken(objectAction);
                break;
            }
            default:
                throw new RuntimeException("Unrecognised object");
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
