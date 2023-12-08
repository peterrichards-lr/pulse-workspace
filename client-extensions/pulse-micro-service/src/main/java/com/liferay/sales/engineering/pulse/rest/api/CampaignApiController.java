package com.liferay.sales.engineering.pulse.rest.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.liferay.sales.engineering.pulse.DuplicateCampaignNameException;
import com.liferay.sales.engineering.pulse.model.Acquisition;
import com.liferay.sales.engineering.pulse.model.Campaign;
import com.liferay.sales.engineering.pulse.model.UrlToken;
import com.liferay.sales.engineering.pulse.rest.BaseRestController;
import com.liferay.sales.engineering.pulse.rest.api.model.CampaignDto;
import com.liferay.sales.engineering.pulse.service.AcquisitionService;
import com.liferay.sales.engineering.pulse.service.CampaignService;
import com.liferay.sales.engineering.pulse.service.UrlTokenService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URISyntaxException;
import java.security.InvalidParameterException;

@RestController
@RequestMapping("/api/campaigns")
public class CampaignApiController extends BaseRestController {
    private static final Log _log = LogFactory.getLog(
            CampaignApiController.class);
    private final AcquisitionService acquisitionService;
    private final CampaignService campaignService;
    private final UrlTokenService urlTokenService;

    @Autowired
    public CampaignApiController(final CampaignService campaignService, final AcquisitionService acquisitionService, final UrlTokenService urlTokenService) {
        this.campaignService = campaignService;
        this.acquisitionService = acquisitionService;
        this.urlTokenService = urlTokenService;
    }

    @PostMapping
    public ResponseEntity<UrlToken> create(
            @AuthenticationPrincipal final Jwt jwt,
            @Valid @RequestBody final CampaignDto campaignDto) {

        try {
            final ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            final String json = mapper.writeValueAsString(campaignDto);
            log(jwt, _log, json);
        } catch (JsonProcessingException e) {
            _log.error("Unable to serialise object ot JSON", e);
        }

        if (campaignService.existsByName(campaignDto.getName())) {
            throw new DuplicateCampaignNameException(campaignDto.getName());
        }

        try {
            final Campaign campaign = campaignService.createCampaign(campaignDto.getName(), campaignDto.getTargetUrl(), campaignDto.getCampaignStatus());
            Acquisition acquisition = null;
            try {
                acquisition = acquisitionService.createAcquisition(campaignDto.getUtmSource(), campaignDto.getUtmMedium(), campaignDto.getUtmContent(), campaignDto.getUtmTerm());
            } catch (InvalidParameterException e) {
                _log.debug("Campaign does not have an acquisition");
            }
            final UrlToken urlToken = urlTokenService.createUrlToken(campaign, acquisition);

            return new ResponseEntity<>(urlToken, HttpStatus.OK);
        } catch (URISyntaxException e) {
            _log.error("Unable to create campaign", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    Page<Campaign> getCampaigns(
            @AuthenticationPrincipal final Jwt jwt,
            @RequestParam(defaultValue = "0") final int page,
            @RequestParam(defaultValue = "20") final int pageSize,
            @RequestParam(defaultValue = "name") final String sortBy) {

        log(jwt, _log);

        final Pageable paging = PageRequest.of(page, pageSize, Sort.by(sortBy));
        return campaignService.findAll(paging);
    }
}
