package com.liferay.sales.engineering.pulse.service.liferay;

import com.liferay.sales.engineering.pulse.service.liferay.model.Acquisition;
import com.liferay.sales.engineering.pulse.service.liferay.model.AcquisitionsResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;
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
import java.util.List;
import java.util.Objects;

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
    public Acquisition createAcquisition(final String utmSource, final String utmMedium, final String utmContent, final String utmTerm) throws URISyntaxException {
        final JSONObject acquisitionJson = new JSONObject();
        acquisitionJson.put("content", utmContent);
        acquisitionJson.put("medium", utmMedium);
        acquisitionJson.put("source", utmSource);
        acquisitionJson.put("term", utmTerm);
        final URI endpoint = this.restEndpoint.toURI();
        final Mono<Acquisition> acquisition = this.webClient.post().uri(endpoint)
                .attributes(getClientRegistrationId())
                .body(BodyInserters.fromValue(acquisitionJson))
                .retrieve()
                .onStatus(HttpStatus::isError, BaseLiferayService::handleLiferayError)
                .bodyToMono(new ParameterizedTypeReference<>() {
                });

        return acquisition.block();
    }

    public List<Acquisition> getAcquisitions() throws URISyntaxException {
        final URI endpoint = restEndpoint.toURI();
        try {
            final Mono<AcquisitionsResponse> campaignResponse = this.webClient.get().uri(endpoint)
                    .attributes(getClientRegistrationId())
                    .retrieve()
                    .onStatus(HttpStatus::isError, BaseLiferayService::handleLiferayError)
                    .bodyToMono(new ParameterizedTypeReference<>() {
                    });

            return Objects.requireNonNull(campaignResponse.block()).getItems();
        } catch (LiferayErrorResponseException ex) {
            if (ex.getStatus() == HttpStatus.NOT_FOUND) {
                throw new NotFoundException(endpoint);
            }
            throw ex;
        }
    }

    @Override
    public Acquisition getByErc(final String erc) throws URISyntaxException {
        final URI endpoint = new URI(this.restEndpoint.toString() + "by-external-reference-code/" + erc);
        try {
            final Mono<Acquisition> acquisition = this.webClient.get().uri(endpoint)
                    .attributes(getClientRegistrationId())
                    .retrieve()
                    .onStatus(HttpStatus::isError, BaseLiferayService::handleLiferayError)
                    .bodyToMono(new ParameterizedTypeReference<>() {
                    });
            return acquisition.block();
        } catch (LiferayErrorResponseException ex) {
            if (ex.getStatus() == HttpStatus.NOT_FOUND) {
                throw new NotFoundException(endpoint);
            }
            throw ex;
        }
    }

    @Override
    protected String getObjectType() {
        return "acquisitions";
    }
}
