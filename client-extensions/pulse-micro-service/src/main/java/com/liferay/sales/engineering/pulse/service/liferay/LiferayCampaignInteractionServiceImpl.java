package com.liferay.sales.engineering.pulse.service.liferay;

import com.liferay.sales.engineering.pulse.PulseException;
import com.liferay.sales.engineering.pulse.model.UrlToken;
import com.liferay.sales.engineering.pulse.service.liferay.model.Interaction;
import com.liferay.sales.engineering.pulse.util.StringUtils;
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
import java.time.LocalDateTime;

@Service
public class LiferayCampaignInteractionServiceImpl extends BaseLiferayService implements LiferayCampaignInteractionService {
    private static final Log _log = LogFactory.getLog(
            LiferayCampaignInteractionServiceImpl.class);

    public LiferayCampaignInteractionServiceImpl(
            @Value("${com.liferay.lxc.dxp.server.protocol}") final String serverProtocol,
            @Value("${com.liferay.lxc.dxp.main.domain}") final String mainDomain,
            final WebClient webClient) throws MalformedURLException {
        super(serverProtocol, mainDomain, webClient);
    }

    private String buildEventProperties(final UrlToken urlToken, final LocalDateTime interactionTime, final String userAgent, final String ipAddress) {
        final JSONObject eventProperties = new JSONObject();
        eventProperties.put("token", urlToken.getToken());
        eventProperties.put("interactionTime", interactionTime);
        eventProperties.put("ipAddress", ipAddress);
        eventProperties.put("userAgent", userAgent);
        return eventProperties.toString();
    }

    @Override
    public Interaction createInteraction(final String event, final UrlToken urlToken, final LocalDateTime interactionTime, final String userAgent, final String ipAddress) {
        final Interaction interaction = new Interaction();
        interaction.setCampaignErc(urlToken.getCampaign().getExternalReferenceCode());
        interaction.setEvent(event);
        interaction.setEventProperties(buildEventProperties(urlToken, interactionTime, userAgent, ipAddress));
        _log.info(String.format("interaction : %s", StringUtils.toJson(interaction)));
        final URI endpoint;
        try {
            endpoint = restEndpoint.toURI();
        } catch (URISyntaxException e) {
            throw new PulseException("Unable to create interaction", e);
        }
        final Mono<Interaction> interactionMono = this.webClient.post().uri(endpoint)
                .attributes(getClientRegistrationId())
                .body(BodyInserters.fromValue(interaction))
                .retrieve()
                .onStatus(HttpStatus::isError, BaseLiferayService::handleLiferayError)
                .bodyToMono(new ParameterizedTypeReference<>() {
                });

        return interactionMono.block();
    }

    @Override
    protected String getObjectType() {
        return "campaigninteractions";
    }
}
