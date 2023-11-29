package com.liferay.sales.engineering.pulse.service;

import com.liferay.sales.engineering.pulse.model.Acquisition;
import com.liferay.sales.engineering.pulse.model.Campaign;
import com.liferay.sales.engineering.pulse.model.UrlToken;
import com.liferay.sales.engineering.pulse.persistence.UrlTokenRepository;
import com.liferay.sales.engineering.pulse.service.liferay.LiferayUrlTokenService;
import com.liferay.sales.engineering.pulse.util.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URISyntaxException;
import java.util.Optional;

@Service
public class UrlTokenServiceImpl implements UrlTokenService {
    private static final Log _log = LogFactory.getLog(
            UrlTokenServiceImpl.class);
    private final LiferayUrlTokenService liferayUrlTokenService;
    private final UrlTokenRepository urlTokenRepository;

    public UrlTokenServiceImpl(final UrlTokenRepository urlTokenRepository, final LiferayUrlTokenService liferayUrlTokenService) {
        this.urlTokenRepository = urlTokenRepository;
        this.liferayUrlTokenService = liferayUrlTokenService;
    }

    @Override
    public UrlToken addUrlToken(final String erc, final String token, final Campaign campaign, final Acquisition acquisition) {
        UrlToken.UrlTokenBuilder tokenBuilder = new UrlToken.UrlTokenBuilder(token, campaign);
        tokenBuilder.withExternalReferenceCode(erc);

        if (acquisition != null) {
            _log.debug("Token has acquisition parameters");
            tokenBuilder.withAcquisition(acquisition);
        }

        UrlToken urlToken = tokenBuilder.build();

        if (urlToken != null) {
            _log.debug("Saving url token");
            _log.debug(urlToken);
            urlTokenRepository.save(urlToken);
        }

        return urlToken;
    }

    private UrlToken addUrlToken(final com.liferay.sales.engineering.pulse.service.liferay.model.UrlToken urlToken, final Campaign campaign, final Acquisition acquisition) {
        return addUrlToken(urlToken.getExternalReferenceCode(), urlToken.getToken(), campaign, acquisition);
    }

    @Override
    public UrlToken createUrlToken(final Campaign campaign, final Acquisition acquisition) throws URISyntaxException {
        final String token = generateUniqueToken();
        final com.liferay.sales.engineering.pulse.service.liferay.model.UrlToken urlToken = this.liferayUrlTokenService.createUrlToken(token, campaign.getExternalReferenceCode(), acquisition == null ? null : acquisition.getExternalReferenceCode());
        return addUrlToken(urlToken, campaign, acquisition);
    }

    @Override
    public Page<UrlToken> findAll(final Pageable paging) {
        return urlTokenRepository.findAll(paging);
    }

    private String generateUniqueToken() {
        String token = StringUtils.generateToken(8);
        Optional<UrlToken> urlToken = urlTokenRepository.findById(token);
        while (!urlToken.isEmpty()) {
            _log.info("Duplicate token. Generating a new one");
            token = StringUtils.generateToken(8);
            urlToken = urlTokenRepository.findById(token);
        }
        return token;
    }

    @Override
    @Transactional
    public void removeUrlToken(final String erc) {
        if (urlTokenRepository.existsByExternalReferenceCode(erc)) {
            urlTokenRepository.deleteByExternalReferenceCode(erc);
            _log.info(String.format("Deleted urlToken %s", erc));
        }
    }
}
