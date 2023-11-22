package com.liferay.sales.engineering.pulse.service;

import com.liferay.sales.engineering.pulse.model.Acquisition;
import com.liferay.sales.engineering.pulse.persistence.AcquisitionRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

@Service
public class AcquisitionServiceImpl implements AcquisitionService {
    private static final Log _log = LogFactory.getLog(
            AcquisitionServiceImpl.class);
    private final AcquisitionRepository acquisitionRepository;

    public AcquisitionServiceImpl(final AcquisitionRepository acquisitionRepository) {
        this.acquisitionRepository = acquisitionRepository;
    }

    public Acquisition createAcquisition(final String utmSource, final String utmMedium, final String utmContent, final String utmTerm) {
        Acquisition.AcquisitionBuilder acquisitionBuilder = new Acquisition.AcquisitionBuilder();
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
}
