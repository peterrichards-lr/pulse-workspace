package com.liferay.sales.engineering.pulse.service.liferay;

import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.function.Consumer;

public abstract class BaseLiferayService {
    protected final URL restEndpoint;
    protected final WebClient webClient;

    protected BaseLiferayService(
            final String scheme,
            final String host,
            final Integer port,
            final WebClient webClient) throws MalformedURLException {
        this.restEndpoint = new URL(scheme, host, port, String.format("/o/c/%s/", getObjectType()));
        this.webClient = webClient;
    }

    protected Consumer<Map<String, Object>> getClientRegistrationId() {
        return ServerOAuth2AuthorizedClientExchangeFilterFunction.clientRegistrationId(
                "pulse-micro-service-oauth-application-headless-server");
    }

    protected abstract String getObjectType();
}
