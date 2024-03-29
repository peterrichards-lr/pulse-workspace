package com.liferay.sales.engineering.pulse.service.liferay;

import com.liferay.sales.engineering.pulse.service.liferay.model.LiferayErrorResponse;
import com.liferay.sales.engineering.pulse.util.UrlUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

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
        this.restEndpoint = UrlUtils.buildUrlFromLiferayProperties(serverProtocol, mainDomain, String.format("/o/c/%s/", getObjectType()));
        _log.info(String.format("%s : %s", "restEndpoint.getHost()", restEndpoint.getHost()));
        _log.info(String.format("%s : %s", "restEndpoint.getPort()", restEndpoint.getPort()));
        _log.debug(String.format("%s : %s", "restEndpoint", this.restEndpoint));
        this.webClient = webClient;
    }

    protected static Mono<LiferayErrorResponseException> handleLiferayError(final ClientResponse clientResponse) {
        if (clientResponse.statusCode() == HttpStatus.UNAUTHORIZED) {
            _log.warn("Unauthorized - " + clientResponse.headers());
            final LiferayErrorResponse errorResponse = new LiferayErrorResponse();
            errorResponse.setStatus(clientResponse.statusCode());
            errorResponse.setTitle(clientResponse.statusCode().getReasonPhrase());
        } else if (clientResponse.statusCode() == HttpStatus.FORBIDDEN) {
            _log.warn("Forbidden - " + clientResponse.headers());
            final LiferayErrorResponse errorResponse = new LiferayErrorResponse();
            errorResponse.setStatus(clientResponse.statusCode());
            errorResponse.setTitle(clientResponse.statusCode().getReasonPhrase());
            return Mono.error(new LiferayErrorResponseException(errorResponse));
        } else if (clientResponse.statusCode() == HttpStatus.NOT_FOUND) {
            _log.info("Not Found - " + clientResponse.headers());
            LiferayErrorResponse errorResponse = new LiferayErrorResponse();
            errorResponse.setStatus(HttpStatus.NOT_FOUND);
            return Mono.error(new LiferayErrorResponseException(errorResponse));
        }
        _log.warn("Unexpected error - status code : " + clientResponse.statusCode());
        return clientResponse.bodyToMono(LiferayErrorResponse.class)
                .flatMap(error -> Mono.error(new LiferayErrorResponseException(error)));
    }

    protected Consumer<Map<String, Object>> getClientRegistrationId() {
        return ServerOAuth2AuthorizedClientExchangeFilterFunction.clientRegistrationId(
                "pulse-micro-service-oauth-application-headless-server");
    }

    protected abstract String getObjectType();
}
