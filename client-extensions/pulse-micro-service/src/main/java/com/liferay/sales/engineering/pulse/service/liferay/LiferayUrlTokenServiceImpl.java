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
            @Value("${com.liferay.lxc.dxp.server.protocol}") final String serverProtocol,
            @Value("${com.liferay.lxc.dxp.main.domain}") final String mainDomain,
            final WebClient webClient) throws MalformedURLException {
        super(serverProtocol, mainDomain, webClient);
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
