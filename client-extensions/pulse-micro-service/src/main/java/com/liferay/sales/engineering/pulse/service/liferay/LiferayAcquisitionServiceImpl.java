package com.liferay.sales.engineering.pulse.service.liferay;

import com.liferay.sales.engineering.pulse.PulseException;
import com.liferay.sales.engineering.pulse.service.liferay.model.Acquisition;
import com.liferay.sales.engineering.pulse.service.liferay.model.AcquisitionsResponse;
import com.liferay.sales.engineering.pulse.util.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;

@Lazy
@Service
public class LiferayAcquisitionServiceImpl extends BaseLiferayService implements LiferayAcquisitionService {
    private static final Log _log = LogFactory.getLog(
            LiferayAcquisitionServiceImpl.class);

    public LiferayAcquisitionServiceImpl(
            @Value("${com.liferay.lxc.dxp.server.protocol}") final String serverProtocol,
            @Value("${com.liferay.lxc.dxp.main.domain}") final String mainDomain,
            final WebClient webClient) throws MalformedURLException {
        super(serverProtocol, mainDomain, webClient);
    }

    @Override
    public Acquisition createAcquisition(final String utmSource, final String utmMedium, final String utmContent, final String utmTerm) {
        final Acquisition acquisition = new Acquisition();
        acquisition.setSource(utmSource);
        acquisition.setMedium(utmMedium);
        acquisition.setContent(utmContent);
        acquisition.setTerm(utmTerm);
        _log.debug(String.format("acquisition : %s", StringUtils.toJson(acquisition)));
        final URI endpoint;
        try {
            endpoint = this.restEndpoint.toURI();
        } catch (URISyntaxException e) {
            throw new PulseException("Unable to create acquisition", e);
        }
        final Mono<Acquisition> acquisitionMono = this.webClient.post().uri(endpoint)
                .attributes(getClientRegistrationId())
                .body(BodyInserters.fromValue(acquisition))
                .retrieve()
                .onStatus(HttpStatus::isError, BaseLiferayService::handleLiferayError)
                .bodyToMono(new ParameterizedTypeReference<>() {
                });

        return acquisitionMono.block();
    }

    public List<Acquisition> getAcquisitions() {
        final URI endpoint;
        try {
            endpoint = restEndpoint.toURI();
        } catch (URISyntaxException e) {
            throw new PulseException("Unable to get acquisitions", e);
        }
        final Mono<AcquisitionsResponse> campaignResponse = this.webClient.get().uri(endpoint)
                .attributes(getClientRegistrationId())
                .retrieve()
                .onStatus(HttpStatus::isError, BaseLiferayService::handleLiferayError)
                .bodyToMono(new ParameterizedTypeReference<>() {
                });

        return Objects.requireNonNull(campaignResponse.block()).getItems();
    }

    @Override
    public Acquisition getByErc(final String erc) {
        final URI endpoint;
        try {
            endpoint = new URI(this.restEndpoint.toString() + "by-external-reference-code/" + erc);
        } catch (URISyntaxException e) {
            throw new PulseException("Unable to get acquisition - " + erc, e);
        }

        final Mono<Acquisition> acquisition = this.webClient.get().uri(endpoint)
                .attributes(getClientRegistrationId())
                .retrieve()
                .onStatus(HttpStatus::isError, BaseLiferayService::handleLiferayError)
                .bodyToMono(new ParameterizedTypeReference<>() {
                });
        return acquisition.block();

    }

    @Override
    protected String getObjectType() {
        return "acquisitions";
    }
}
