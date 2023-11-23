package com.liferay.sales.engineering.pulse.service.liferay;

import com.liferay.sales.engineering.pulse.service.liferay.model.UrlToken;

import java.net.URISyntaxException;
import java.util.List;

public interface LiferayUrlTokenService {
    List<UrlToken> getUrlTokens() throws URISyntaxException;
}
