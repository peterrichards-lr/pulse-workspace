package com.liferay.sales.engineering.pulse.service.liferay;

import com.liferay.sales.engineering.pulse.service.liferay.model.Acquisition;

import java.net.URISyntaxException;
import java.util.List;

public interface LiferayAcquisitionService {
    Acquisition createAcquisition(final String utmSource, final String utmMedium, final String utmContent, final String utmTerm) throws URISyntaxException;

    List<Acquisition> getAcquisitions() throws URISyntaxException;

    Acquisition getByErc(final String erc) throws URISyntaxException;
}
