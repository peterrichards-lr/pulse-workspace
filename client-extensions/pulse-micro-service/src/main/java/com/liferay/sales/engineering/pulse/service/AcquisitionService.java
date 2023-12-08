package com.liferay.sales.engineering.pulse.service;

import com.liferay.sales.engineering.pulse.model.Acquisition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.net.URISyntaxException;

public interface AcquisitionService {
    Acquisition addAcquisition(final String erc, final String utmSource, final String utmMedium, final String utmContent, final String utmTerm);

    Acquisition createAcquisition(final String utmSource, final String utmMedium, final String utmContent, final String utmTerm) throws URISyntaxException;

    Page<Acquisition> findAll(Pageable paging);

    void removeAcquisition(final String erc);

    void removeAll();

    Acquisition retrieveAcquisition(final String erc);

    Acquisition updateAcquisition(final String erc, final String utmSource, final String utmMedium, final String utmContent, final String utmTerm);
}
