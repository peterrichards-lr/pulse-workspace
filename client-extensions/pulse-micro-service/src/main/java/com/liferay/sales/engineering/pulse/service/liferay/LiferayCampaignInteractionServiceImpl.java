package com.liferay.sales.engineering.pulse.service.liferay;

import com.liferay.sales.engineering.pulse.model.Campaign;
import com.liferay.sales.engineering.pulse.service.liferay.model.Interaction;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.MalformedURLException;
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

    private Interaction buildInteraction(final Campaign campaign, final LocalDateTime interactionTime, final String userAgent, final String ipAddress) {
        final Interaction interaction = new Interaction(campaign);
        interaction.setEvent("Link navigation");
        final JSONObject eventProperties = new JSONObject();
        eventProperties.put("interactionTime", interactionTime);
        eventProperties.put("ipAddress", ipAddress);
        eventProperties.put("userAgent", userAgent);
        interaction.setEventProperties(eventProperties.toString());
        return interaction;
    }

    @Override
    public Long createInteraction(final Campaign campaign, final LocalDateTime interactionTime, final String userAgent, final String ipAddress) throws URISyntaxException {
        final Interaction interaction = buildInteraction(campaign, interactionTime, userAgent, ipAddress);

        _log.info(String.format("%s : %s", "Interaction (before)", interaction));

        final Mono<Interaction> interactionMono = this.webClient.post().uri(restEndpoint.toURI())
                .attributes(getClientRegistrationId())
                .body(BodyInserters.fromValue(interaction))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<>() {
                });

        final Interaction afterInteraction = interactionMono.block();
        _log.info(String.format("%s : %s", "Interaction (after)", afterInteraction));

        return afterInteraction.getId();
    }

    @Override
    protected String getObjectType() {
        return "campaigninteractions";
    }
}
