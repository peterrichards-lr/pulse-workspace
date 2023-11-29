package com.liferay.sales.engineering.pulse.service;

import com.liferay.sales.engineering.pulse.model.Acquisition;
import com.liferay.sales.engineering.pulse.model.Campaign;
import com.liferay.sales.engineering.pulse.model.UrlToken;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.net.URISyntaxException;

public interface UrlTokenService {
    UrlToken addUrlToken(final String erc, final String token, final Campaign campaign, final Acquisition acquisition);

    UrlToken createUrlToken(final Campaign campaign, final Acquisition acquisition) throws URISyntaxException;

    Page<UrlToken> findAll(Pageable paging);

    void removeUrlToken(final String erc);
}
