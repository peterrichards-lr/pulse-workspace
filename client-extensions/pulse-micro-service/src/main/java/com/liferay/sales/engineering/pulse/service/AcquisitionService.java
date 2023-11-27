package com.liferay.sales.engineering.pulse.service;

import com.liferay.sales.engineering.pulse.model.Acquisition;

import java.net.URISyntaxException;

public interface AcquisitionService {
    Acquisition addAcquisition(final String erc, final String utmSource, final String utmMedium, final String utmContent, final String utmTerm);

    Acquisition createAcquisition(final String utmSource, final String utmMedium, final String utmContent, final String utmTerm) throws URISyntaxException;
}
