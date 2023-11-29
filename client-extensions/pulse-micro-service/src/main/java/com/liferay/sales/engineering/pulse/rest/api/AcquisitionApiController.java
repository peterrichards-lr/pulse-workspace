package com.liferay.sales.engineering.pulse.rest.api;

import com.liferay.sales.engineering.pulse.model.Acquisition;
import com.liferay.sales.engineering.pulse.model.UrlToken;
import com.liferay.sales.engineering.pulse.rest.BaseRestController;
import com.liferay.sales.engineering.pulse.service.AcquisitionService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/acquisitions")
public class AcquisitionApiController extends BaseRestController {
    private static final Log _log = LogFactory.getLog(
            AcquisitionApiController.class);
    private final AcquisitionService acquisitionService;

    public AcquisitionApiController(final AcquisitionService acquisitionService) {
        this.acquisitionService = acquisitionService;
    }

    @GetMapping
    Page<Acquisition> getUrlTokens(
            @AuthenticationPrincipal Jwt jwt,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int pageSize,
            @RequestParam(defaultValue = "externalReferenceCode") String sortBy) {

        log(jwt, _log);

        final Pageable paging = PageRequest.of(page, pageSize, Sort.by(sortBy));
        return acquisitionService.findAll(paging);
    }
}
