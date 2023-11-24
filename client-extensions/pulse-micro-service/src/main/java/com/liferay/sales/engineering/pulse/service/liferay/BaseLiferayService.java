package com.liferay.sales.engineering.pulse.service.liferay;

import com.liferay.sales.engineering.pulse.util.UrlUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.function.Consumer;

public abstract class BaseLiferayService {
    private static final Log _log = LogFactory.getLog(
            BaseLiferayService.class);
    protected final URL restEndpoint;
    protected final WebClient webClient;

    protected BaseLiferayService(
            final String serverProtocol,
            final String mainDomain,
            final WebClient webClient) throws MalformedURLException {
        _log.debug(String.format("%s : %s", "com.liferay.lxc.dxp.server.protocol", serverProtocol));
        _log.debug(String.format("%s : %s", "com.liferay.lxc.dxp.main.domain", mainDomain));
        this.restEndpoint = UrlUtils.buildUrlFromLxcProperties(serverProtocol, mainDomain, String.format("/o/c/%s/", getObjectType()));
        _log.info(String.format("%s : %s", "restEndpoint.getHost()", restEndpoint.getHost()));
        _log.info(String.format("%s : %s", "restEndpoint.getPort()", restEndpoint.getPort()));
        _log.debug(String.format("%s : %s", "restEndpoint", this.restEndpoint));
        this.webClient = webClient;
    }

    protected Consumer<Map<String, Object>> getClientRegistrationId() {
        return ServerOAuth2AuthorizedClientExchangeFilterFunction.clientRegistrationId(
                "pulse-micro-service-oauth-application-headless-server");
    }

    protected abstract String getObjectType();
}
