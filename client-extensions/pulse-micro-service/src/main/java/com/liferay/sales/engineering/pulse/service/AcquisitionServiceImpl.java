package com.liferay.sales.engineering.pulse.service;

import com.liferay.sales.engineering.pulse.model.Acquisition;
import com.liferay.sales.engineering.pulse.persistence.AcquisitionRepository;
import com.liferay.sales.engineering.pulse.service.liferay.LiferayAcquisitionService;
import com.liferay.sales.engineering.pulse.util.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URISyntaxException;
import java.security.InvalidParameterException;

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
        if (StringUtils.isBlank(utmSource) &&
                StringUtils.isBlank(utmMedium) &&
                StringUtils.isBlank(utmContent) &&
                StringUtils.isBlank(utmTerm)) {
            throw new InvalidParameterException("At least one acquisition parameter must have value");
        }
        final com.liferay.sales.engineering.pulse.service.liferay.model.Acquisition acquisition = liferayAcquisitionService.createAcquisition(utmSource, utmMedium, utmContent, utmTerm);
        return addAcquisition(acquisition);
    }

    @Override
    public Page<Acquisition> findAll(final Pageable paging) {
        return acquisitionRepository.findAll(paging);
    }

    @Override
    @Transactional
    public void removeAcquisition(final String erc) {
        if (acquisitionRepository.existsByExternalReferenceCode(erc)) {
            acquisitionRepository.deleteByExternalReferenceCode(erc);
            _log.info(String.format("Deleted acquisition %s", erc));
        }
    }

    @Override
    public Acquisition retrieveAcquisition(final String erc) {
        return acquisitionRepository.findByExternalReferenceCode(erc);
    }

    @Override
    public Acquisition updateAcquisition(final String erc, final String utmSource, final String utmMedium, final String utmContent, final String utmTerm) {
        Acquisition acquisition = retrieveAcquisition(erc);
        acquisition.setSource(utmSource);
        acquisition.setMedium(utmMedium);
        acquisition.setContent(utmContent);
        acquisition.setTerm(utmTerm);

        _log.debug(String.format("acquisition : %s", acquisition));

        acquisitionRepository.save(acquisition);
        return acquisition;
    }
}
