package com.liferay.sales.engineering.pulse.service.liferay;

import com.liferay.sales.engineering.pulse.service.liferay.model.Campaign;
import com.liferay.sales.engineering.pulse.service.liferay.model.CampaignsResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
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
public class LiferayCampaignServiceImpl extends BaseLiferayService implements LiferayCampaignService {
    private static final Log _log = LogFactory.getLog(
            LiferayCampaignServiceImpl.class);

    @Autowired
    public LiferayCampaignServiceImpl(
            @Value("${com.liferay.lxc.dxp.server.protocol}") final String serverProtocol,
            @Value("${com.liferay.lxc.dxp.main.domain}") final String mainDomain,
            final WebClient webClient) throws MalformedURLException {
        super(serverProtocol, mainDomain, webClient);
    }

    @Override
    public Campaign createCampaign(final String name, final String targetUrl, final String status) throws URISyntaxException {
        final JSONObject campaignJson = new JSONObject();
        campaignJson.put("name", name);
        campaignJson.put("targetUrl", targetUrl);
        campaignJson.put("status", status);
        final Mono<Campaign> campaign = this.webClient.post().uri(this.restEndpoint.toURI())
                .attributes(getClientRegistrationId())
                .body(BodyInserters.fromValue(campaignJson))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<>() {
                });

        return campaign.block();
    }

    @Override
    public Campaign getByErc(final String erc) throws URISyntaxException {
        final URI endpoint = new URI(this.restEndpoint.toString() + "/by-external-reference-code/" + erc);
        final Mono<Campaign> campaign = this.webClient.get().uri(endpoint)
                .attributes(getClientRegistrationId())
                .retrieve().bodyToMono(new ParameterizedTypeReference<>() {
                });
        return campaign.block();
    }

    public List<Campaign> getCampaigns() throws URISyntaxException {
        Mono<CampaignsResponse> campaignResponse = webClient.get().uri(restEndpoint.toURI())
                .attributes(getClientRegistrationId())
                .retrieve().bodyToMono(new ParameterizedTypeReference<>() {
                });

        return Objects.requireNonNull(campaignResponse.block()).getItems();
    }

    @Override
    protected String getObjectType() {
        return "campaigns";
    }
}
