package com.liferay.sales.engineering.pulse.service.liferay;

import com.liferay.sales.engineering.pulse.service.liferay.model.Acquisition;

import java.util.List;

public interface LiferayAcquisitionService {
    Acquisition createAcquisition(final String utmSource, final String utmMedium, final String utmContent, final String utmTerm);

    List<Acquisition> getAcquisitions();

    Acquisition getByErc(final String erc);
}
