package com.liferay.sales.engineering.pulse.service;

import com.liferay.sales.engineering.pulse.model.Acquisition;
import com.liferay.sales.engineering.pulse.model.Campaign;
import com.liferay.sales.engineering.pulse.model.UrlToken;
import com.liferay.sales.engineering.pulse.persistence.UrlTokenRepository;
import com.liferay.sales.engineering.pulse.util.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UrlTokenServiceImpl implements UrlTokenService {
    private static final Log _log = LogFactory.getLog(
            UrlTokenServiceImpl.class);
    private final UrlTokenRepository urlTokenRepository;

    public UrlTokenServiceImpl(final UrlTokenRepository urlTokenRepository) {
        this.urlTokenRepository = urlTokenRepository;
    }

    public UrlToken createUrlToken(final Campaign campaign, final Acquisition acquisition) {
        final String token = generateUniqueToken();
        UrlToken.UrlTokenBuilder tokenBuilder = new UrlToken.UrlTokenBuilder(token, campaign);

        if (acquisition != null) {
            _log.debug("Token has acquisition parameters");
            tokenBuilder.withAcquisition(acquisition);
        }

        UrlToken urlToken = tokenBuilder.build();

        if (urlToken != null) {
            _log.debug("Saving url token");
            urlTokenRepository.save(urlToken);
        }

        return urlToken;
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
}
