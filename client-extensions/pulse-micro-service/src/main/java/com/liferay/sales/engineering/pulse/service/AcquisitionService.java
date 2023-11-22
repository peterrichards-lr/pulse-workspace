package com.liferay.sales.engineering.pulse.service;

import com.liferay.sales.engineering.pulse.model.Acquisition;

public interface AcquisitionService {
    Acquisition createAcquisition(final String utmSource, final String utmMedium, final String utmContent, final String utmTerm);
}
