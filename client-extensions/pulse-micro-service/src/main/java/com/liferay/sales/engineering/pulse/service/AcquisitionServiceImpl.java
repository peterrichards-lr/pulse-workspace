package com.liferay.sales.engineering.pulse.service;

import com.liferay.sales.engineering.pulse.model.Acquisition;
import com.liferay.sales.engineering.pulse.persistence.AcquisitionRepository;
import com.liferay.sales.engineering.pulse.service.liferay.LiferayAcquisitionService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import java.net.URISyntaxException;

@Service
public class AcquisitionServiceImpl implements AcquisitionService {
    private static final Log _log = LogFactory.getLog(
            AcquisitionServiceImpl.class);
    private final AcquisitionRepository acquisitionRepository;
    private final LiferayAcquisitionService liferayAcquisitionService;

    public AcquisitionServiceImpl(final AcquisitionRepository acquisitionRepository, final LiferayAcquisitionService liferayAcquisitionService) {
        this.acquisitionRepository = acquisitionRepository;
        this.liferayAcquisitionService = liferayAcquisitionService;
    }

    private Acquisition addAcquisition(com.liferay.sales.engineering.pulse.service.liferay.model.Acquisition acquisition) {
        return addAcquisition(acquisition.getExternalReferenceCode(), acquisition.getSource(), acquisition.getMedium(), acquisition.getContent(), acquisition.getTerm());
    }

    @Override
    public Acquisition addAcquisition(final String erc, final String utmSource, final String utmMedium, final String utmContent, final String utmTerm) {
        Acquisition.AcquisitionBuilder acquisitionBuilder = new Acquisition.AcquisitionBuilder(erc);
        acquisitionBuilder.withSource(utmSource);
        acquisitionBuilder.withMedium(utmMedium);
        acquisitionBuilder.withContent(utmContent);
        acquisitionBuilder.withTerm(utmTerm);
        Acquisition acquisition = acquisitionBuilder.build();

        if (acquisition != null) {
            _log.debug("Saving acquisition");
            acquisitionRepository.save(acquisition);
        }
        return acquisition;
    }

    @Override
    public Acquisition createAcquisition(final String utmSource, final String utmMedium, final String utmContent, final String utmTerm) throws URISyntaxException {
        final com.liferay.sales.engineering.pulse.service.liferay.model.Acquisition acquisition = liferayAcquisitionService.createAcquisition(utmSource, utmMedium, utmContent, utmTerm);
        return addAcquisition(acquisition);
    }
}
