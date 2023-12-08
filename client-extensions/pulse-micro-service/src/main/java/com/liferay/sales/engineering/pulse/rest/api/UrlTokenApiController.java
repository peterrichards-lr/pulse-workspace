package com.liferay.sales.engineering.pulse.rest.api;


import com.liferay.sales.engineering.pulse.model.UrlToken;
import com.liferay.sales.engineering.pulse.rest.BaseRestController;
import com.liferay.sales.engineering.pulse.service.UrlTokenService;
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
@RequestMapping("/api/url-tokens")
public class UrlTokenApiController extends BaseRestController {
    private static final Log _log = LogFactory.getLog(
            UrlTokenApiController.class);
    private final UrlTokenService urlTokenService;

    UrlTokenApiController(final UrlTokenService urlTokenService) {
        this.urlTokenService = urlTokenService;
    }

    @GetMapping
    Page<UrlToken> getUrlTokens(
            @AuthenticationPrincipal final Jwt jwt,
            @RequestParam(defaultValue = "0") final int page,
            @RequestParam(defaultValue = "20") final int pageSize,
            @RequestParam(defaultValue = "token") final String sortBy) {

        log(jwt, _log);

        final Pageable paging = PageRequest.of(page, pageSize, Sort.by(sortBy));
        return urlTokenService.findAll(paging);
    }
}
