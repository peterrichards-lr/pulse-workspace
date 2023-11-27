package com.liferay.sales.engineering.pulse.service.liferay;

import com.liferay.sales.engineering.pulse.service.liferay.model.UrlToken;

import java.net.URISyntaxException;
import java.util.List;

public interface LiferayUrlTokenService {
    UrlToken createUrlToken(String token, String campaignErc, String acquisitionErc) throws URISyntaxException;

    List<UrlToken> getUrlTokens() throws URISyntaxException;
}
