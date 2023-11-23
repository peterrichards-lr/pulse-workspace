package com.liferay.sales.engineering.pulse.service.liferay;

import com.liferay.sales.engineering.pulse.service.liferay.model.UrlToken;
import com.liferay.sales.engineering.pulse.service.liferay.model.UrlTokensResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;

@Service
public class LiferayUrlTokenServiceImpl extends BaseLiferayService implements LiferayUrlTokenService {
    private static final Log _log = LogFactory.getLog(
            LiferayUrlTokenServiceImpl.class);

    public LiferayUrlTokenServiceImpl(
            @Value("${pulse.liferay.base-endpoint.scheme}") final String scheme,
            @Value("${pulse.liferay.base-endpoint.host}") final String host,
            @Value("${pulse.liferay.base-endpoint.port}") final Integer port,
            final WebClient webClient) throws MalformedURLException {
        super(scheme, host, port, webClient);
    }

    @Override
    protected String getObjectType() {
        return "urltokens";
    }

    @Override
    public List<UrlToken> getUrlTokens() throws URISyntaxException {
        Mono<UrlTokensResponse> urlTokensResponse = this.webClient.get().uri(this.restEndpoint.toURI())
                .attributes(getClientRegistrationId())
                .retrieve().bodyToMono(new ParameterizedTypeReference<>() {
                });

        return Objects.requireNonNull(urlTokensResponse.block()).getItems();
    }
}
