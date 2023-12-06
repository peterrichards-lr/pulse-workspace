package com.liferay.sales.engineering.pulse.service.liferay;

import com.liferay.sales.engineering.pulse.DuplicateCampaignNameException;
import com.liferay.sales.engineering.pulse.service.liferay.model.Campaign;
import com.liferay.sales.engineering.pulse.service.liferay.model.CampaignStatus;
import com.liferay.sales.engineering.pulse.service.liferay.model.CampaignsResponse;
import com.liferay.sales.engineering.pulse.util.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
        return createCampaign(name, null, targetUrl, status, null, null);
    }

    @Override
    public Campaign createCampaign(final String name, final String description, final String targetUrl, final String status, final LocalDateTime startDate, final LocalDateTime endDate) throws URISyntaxException {
        try {
            final Campaign campaign = new Campaign();
            campaign.setName(name);
            campaign.setTargetUrl(targetUrl);
            if (StringUtils.isNotBlank(description))
                campaign.setDescription(description);
            if (startDate != null)
                campaign.setBegin(startDate);
            if (endDate != null)
                campaign.setEnd(endDate);

            final CampaignStatus campaignStatus = new CampaignStatus();
            campaignStatus.setKey(status);
            campaign.setCampaignStatus(campaignStatus);
            _log.info(String.format("campaign : %s", StringUtils.toJson(campaign)));
            final URI endpoint = this.restEndpoint.toURI();
            final Mono<Campaign> campaignMono = this.webClient.post().uri(endpoint)
                    .attributes(getClientRegistrationId())
                    .body(BodyInserters.fromValue(campaign))
                    .retrieve()
                    .onStatus(HttpStatus::isError, BaseLiferayService::handleLiferayError)
                    .bodyToMono(new ParameterizedTypeReference<>() {
                    });

            return campaignMono.block();
        } catch (LiferayErrorResponseException e) {
            if (e.getStatus() == HttpStatus.BAD_REQUEST &&
                    e.getTitle().contains("already in use")) {
                throw new DuplicateCampaignNameException(name);
            }
            throw e;
        }
    }


    @Override
    public Campaign getByErc(final String erc) throws URISyntaxException {
        final URI endpoint = new URI(this.restEndpoint.toString() + "by-external-reference-code/" + erc);
        final Mono<Campaign> campaign = this.webClient.get().uri(endpoint)
                .attributes(getClientRegistrationId())
                .retrieve()
                .onStatus(HttpStatus::isError, BaseLiferayService::handleLiferayError)
                .bodyToMono(new ParameterizedTypeReference<>() {
                });
        return campaign.block();
    }

    public List<Campaign> getCampaigns() throws URISyntaxException {
        final URI endpoint = restEndpoint.toURI();
        final Mono<CampaignsResponse> campaignResponse = webClient.get().uri(endpoint)
                .attributes(getClientRegistrationId())
                .retrieve()
                .onStatus(HttpStatus::isError, BaseLiferayService::handleLiferayError)
                .bodyToMono(new ParameterizedTypeReference<>() {
                });

        return Objects.requireNonNull(campaignResponse.block()).getItems();
    }

    @Override
    protected String getObjectType() {
        return "campaigns";
    }
}
