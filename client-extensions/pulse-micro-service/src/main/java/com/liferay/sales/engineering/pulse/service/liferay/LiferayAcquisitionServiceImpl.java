package com.liferay.sales.engineering.pulse.service.liferay;

import com.liferay.sales.engineering.pulse.service.liferay.model.Acquisition;
import com.liferay.sales.engineering.pulse.service.liferay.model.AcquisitionsResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;

@Service
public class LiferayAcquisitionServiceImpl extends BaseLiferayService implements LiferayAcquisitionService {
    private static final Log _log = LogFactory.getLog(
            LiferayAcquisitionServiceImpl.class);

    public LiferayAcquisitionServiceImpl(
            @Value("${pulse.liferay.base-endpoint.scheme}") final String scheme,
            @Value("${pulse.liferay.base-endpoint.host}") final String host,
            @Value("${pulse.liferay.base-endpoint.port}") final Integer port,
            final WebClient webClient) throws MalformedURLException {
        super(scheme, host, port, webClient);
    }

    public List<Acquisition> getAcquisitions() throws URISyntaxException {
        final Mono<AcquisitionsResponse> campaignResponse = this.webClient.get().uri(restEndpoint.toURI())
                .attributes(getClientRegistrationId())
                .retrieve().bodyToMono(new ParameterizedTypeReference<>() {
                });

        return Objects.requireNonNull(campaignResponse.block()).getItems();
    }

    @Override
    public Acquisition getByErc(final String erc) throws URISyntaxException {
        final URI endpoint = new URI(this.restEndpoint.toString() + "/by-external-reference-code/" + erc);
        final Mono<Acquisition> acquisition = this.webClient.get().uri(endpoint)
                .attributes(getClientRegistrationId())
                .retrieve().bodyToMono(new ParameterizedTypeReference<>() {
                });
        return acquisition.block();
    }

    @Override
    protected String getObjectType() {
        return "acquisitions";
    }
}
