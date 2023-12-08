package com.liferay.sales.engineering.pulse.rest.object;

import com.liferay.sales.engineering.pulse.rest.object.helper.AcquisitionObjectActionHelper;
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
@RequestMapping("/object/acquisitions")
public class AcquisitionObjectActionController extends BaseObjectActionController {
    private static final String OBJECT_ENTRY_PROPERTY_NAME = "objectEntryDTOAcquisition";
    private static final Log _log = LogFactory.getLog(
            AcquisitionObjectActionController.class);
    private final AcquisitionObjectActionHelper acquisitionObjectActionHelper;

    public AcquisitionObjectActionController(final AcquisitionObjectActionHelper acquisitionObjectActionHelper) {
        this.acquisitionObjectActionHelper = acquisitionObjectActionHelper;
    }

    @PostMapping("/delete")
    public ResponseEntity<String> delete(
            @AuthenticationPrincipal final Jwt jwt, @RequestBody final String json) {
        final ObjectAction objectAction = super.parseRequest(jwt, json, OBJECT_ENTRY_PROPERTY_NAME, _log);

        if (objectAction.isDelete()) {
            acquisitionObjectActionHelper.removeAcquisition(objectAction);
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/update")
    public ResponseEntity<String> update(
            @AuthenticationPrincipal final Jwt jwt, @RequestBody final String json) {
        final ObjectAction objectAction = super.parseRequest(jwt, json, OBJECT_ENTRY_PROPERTY_NAME, _log);

        if (objectAction.isUpdate()) {
            acquisitionObjectActionHelper.updateAcquisition(objectAction);
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
