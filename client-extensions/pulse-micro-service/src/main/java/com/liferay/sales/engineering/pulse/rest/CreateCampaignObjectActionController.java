package com.liferay.sales.engineering.pulse.rest;

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

@RequestMapping("/api/campaigns")
@RestController
public class CreateCampaignObjectActionController extends BaseController {
    private static final Log _log = LogFactory.getLog(
            CreateCampaignObjectActionController.class);

    @PostMapping
    public ResponseEntity<String> post(
            @AuthenticationPrincipal Jwt jwt, @RequestBody String json) {

        log(jwt, _log, json);

        return new ResponseEntity<>(json, HttpStatus.OK);
    }
}
