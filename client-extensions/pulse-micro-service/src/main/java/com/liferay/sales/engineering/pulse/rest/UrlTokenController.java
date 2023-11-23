package com.liferay.sales.engineering.pulse.rest;


import com.liferay.sales.engineering.pulse.model.UrlToken;
import com.liferay.sales.engineering.pulse.persistence.UrlTokenRepository;
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
public class UrlTokenController extends BaseController {
    private static final Log _log = LogFactory.getLog(
            UrlTokenController.class);
    private final UrlTokenRepository urlTokenRepository;

    UrlTokenController(final UrlTokenRepository urlTokenRepository) {
        this.urlTokenRepository = urlTokenRepository;
    }

    @GetMapping
    Page<UrlToken> getUrlTokens(
            @AuthenticationPrincipal Jwt jwt,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int pageSize,
            @RequestParam(defaultValue = "token") String sortBy) {

        log(jwt, _log);

        final Pageable paging = PageRequest.of(page, pageSize, Sort.by(sortBy));
        return urlTokenRepository.findAll(paging);
    }
}
