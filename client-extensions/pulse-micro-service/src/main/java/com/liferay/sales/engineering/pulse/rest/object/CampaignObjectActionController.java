package com.liferay.sales.engineering.pulse.rest.object;

import com.liferay.sales.engineering.pulse.rest.object.helper.CampaignObjectActionHelper;
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
@RequestMapping("/object/campaigns")
public class CampaignObjectActionController extends BaseObjectActionController {
    private static final String OBJECT_ENTRY_PROPERTY_NAME = "objectEntryDTOCampaign";
    private static final Log _log = LogFactory.getLog(
            CampaignObjectActionController.class);
    private final CampaignObjectActionHelper campaignObjectActionHelper;

    public CampaignObjectActionController(final CampaignObjectActionHelper campaignObjectActionHelper) {
        this.campaignObjectActionHelper = campaignObjectActionHelper;
    }

    @PostMapping("/delete")
    public ResponseEntity<String> delete(
            @AuthenticationPrincipal final Jwt jwt, @RequestBody final String json) {
        final ObjectAction objectAction = super.parseRequest(jwt, json, OBJECT_ENTRY_PROPERTY_NAME, _log);

        if (objectAction.isDelete()) {
            campaignObjectActionHelper.removeCampaign(objectAction);
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/update")
    public ResponseEntity<String> update(
            @AuthenticationPrincipal final Jwt jwt, @RequestBody final String json) {
        final ObjectAction objectAction = super.parseRequest(jwt, json, OBJECT_ENTRY_PROPERTY_NAME, _log);
        if (objectAction.isUpdate()) {
            campaignObjectActionHelper.updateCampaign(objectAction);
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
