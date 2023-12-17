package com.liferay.sales.engineering.pulse.service.liferay;

import com.liferay.sales.engineering.pulse.service.liferay.model.UrlToken;

import java.util.List;

public interface LiferayUrlTokenService {
    UrlToken createUrlToken(String token, String campaignErc, String acquisitionErc);

    List<UrlToken> getUrlTokens();
}
