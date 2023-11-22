package com.liferay.sales.engineering.pulse.service;

import com.liferay.sales.engineering.pulse.model.Acquisition;
import com.liferay.sales.engineering.pulse.model.Campaign;
import com.liferay.sales.engineering.pulse.model.UrlToken;

public interface UrlTokenService {
    UrlToken createUrlToken(final Campaign campaign, final Acquisition acquisition);
}
