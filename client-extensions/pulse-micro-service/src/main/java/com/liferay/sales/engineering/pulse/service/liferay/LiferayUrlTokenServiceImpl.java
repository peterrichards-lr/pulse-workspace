package com.liferay.sales.engineering.pulse.service.liferay;

import com.liferay.sales.engineering.pulse.service.liferay.model.UrlToken;
import com.liferay.sales.engineering.pulse.service.liferay.model.UrlTokensResponse;
import com.liferay.sales.engineering.pulse.util.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;

@Service
public class LiferayUrlTokenServiceImpl extends BaseLiferayService implements LiferayUrlTokenService {
    private static final Log _log = LogFactory.getLog(
            LiferayUrlTokenServiceImpl.class);

    public LiferayUrlTokenServiceImpl(
            @Value("${com.liferay.lxc.dxp.server.protocol}") final String serverProtocol,
            @Value("${com.liferay.lxc.dxp.main.domain}") final String mainDomain,
            final WebClient webClient) throws MalformedURLException {
        super(serverProtocol, mainDomain, webClient);
    }

    @Override
    public UrlToken createUrlToken(final String token, final String campaignErc, final String acquisitionErc) throws URISyntaxException {
        try {
            final UrlToken urlToken = new UrlToken();
            urlToken.setToken(token);
            urlToken.setCampaignErc(campaignErc);
            if (StringUtils.isNotBlank(acquisitionErc))
                urlToken.setAcquisitionErc(acquisitionErc);
            _log.info(String.format("urlToken : %s", StringUtils.toJson(urlToken)));
            final URI endpoint = this.restEndpoint.toURI();
            final Mono<UrlToken> urlTokenMono = this.webClient.post().uri(endpoint)
                    .attributes(getClientRegistrationId())
                    .body(BodyInserters.fromValue(urlToken))
                    .retrieve()
                    .onStatus(HttpStatus::isError, BaseLiferayService::handleLiferayError)
                    .bodyToMono(new ParameterizedTypeReference<>() {
                    });

            return urlTokenMono.block();
        } catch (LiferayErrorResponseException e) {
            if (e.getStatus() == HttpStatus.FORBIDDEN) {
                _log.info("No permissions to create url token");
            }
            throw e;
        }
    }

    @Override
    protected String getObjectType() {
        return "urltokens";
    }

    @Override
    public List<UrlToken> getUrlTokens() throws URISyntaxException {
        final URI endpoint = this.restEndpoint.toURI();
        final Mono<UrlTokensResponse> urlTokensResponseMono = this.webClient.get().uri(endpoint)
                .attributes(getClientRegistrationId())
                .retrieve()
                .onStatus(HttpStatus::isError, BaseLiferayService::handleLiferayError)
                .bodyToMono(new ParameterizedTypeReference<>() {
                });

        final UrlTokensResponse urlTokensResponse = urlTokensResponseMono.block();
        if (urlTokensResponse == null) {
            return Collections.emptyList();
        }
        return urlTokensResponse.getItems();
    }
}
