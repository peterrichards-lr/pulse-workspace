package com.liferay.sales.engineering.pulse.rest;

import com.liferay.sales.engineering.pulse.model.Acquisition;
import com.liferay.sales.engineering.pulse.model.Campaign;
import com.liferay.sales.engineering.pulse.model.UrlToken;
import com.liferay.sales.engineering.pulse.rest.model.CampaignDto;
import com.liferay.sales.engineering.pulse.service.AcquisitionService;
import com.liferay.sales.engineering.pulse.service.CampaignService;
import com.liferay.sales.engineering.pulse.service.UrlTokenService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequestMapping("/api/campaigns")
@RestController
public class CampaignController extends BaseController {
    private static final Log _log = LogFactory.getLog(
            CampaignController.class);
    private final AcquisitionService acquisitionService;
    private final CampaignService campaignService;
    private final UrlTokenService urlTokenService;

    @Autowired
    public CampaignController(final CampaignService campaignService, final AcquisitionService acquisitionService, final UrlTokenService urlTokenService) {
        this.campaignService = campaignService;
        this.acquisitionService = acquisitionService;
        this.urlTokenService = urlTokenService;
    }

    @PostMapping
    public ResponseEntity<UrlToken> create(
            @AuthenticationPrincipal Jwt jwt, @Valid @RequestBody CampaignDto campaignDto) {

        log(jwt, _log, campaignDto.toString());

        if (campaignService.existsByName(campaignDto.getName())) {
            throw new DuplicateCampaignNameException(campaignDto.getName());
        }

        final Campaign campaign = campaignService.createCampaign(campaignDto.getName(), campaignDto.getCampaignUrl(), campaignDto.getStatus());
        final Acquisition acquisition = acquisitionService.createAcquisition(campaignDto.getUtmSource(), campaignDto.getUtmMedium(), campaignDto.getUtmContent(), campaignDto.getUtmTerm());
        final UrlToken urlToken = urlTokenService.createUrlToken(campaign, acquisition);

        return new ResponseEntity<>(urlToken, HttpStatus.OK);
    }
}
