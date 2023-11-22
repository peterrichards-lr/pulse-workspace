package com.liferay.sales.engineering.pulse;

import com.liferay.client.extension.util.spring.boot.ClientExtensionUtilSpringBootComponentScan;
import com.liferay.client.extension.util.spring.boot.LiferayOAuth2Util;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.oauth2.client.*;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.MalformedURLException;
import java.net.URL;

import static org.springframework.boot.SpringApplication.run;

@Import(ClientExtensionUtilSpringBootComponentScan.class)
@SpringBootApplication
@EnableWebFluxSecurity
public class PulseApplication {
    private static final Log _log = LogFactory.getLog(
            PulseApplication.class);
    @Value("${liferay.oauth.application.external.reference.headless-server}")
    private String _liferayOAuthApplicationExternalReferenceCodes;

    public static void main(String[] args) {
        run(PulseApplication.class, args);
    }

    @Bean
    public AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager authorizedClientManager(
            ReactiveClientRegistrationRepository clientRegistrationRepository,
            ReactiveOAuth2AuthorizedClientService reactiveOAuth2AuthorizedClientService) {

        ReactiveOAuth2AuthorizedClientProvider authorizedClientProvider =
                ReactiveOAuth2AuthorizedClientProviderBuilder.builder().clientCredentials().build();

        AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager authorizedClientManager =
                new AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager(
                        clientRegistrationRepository, reactiveOAuth2AuthorizedClientService);

        authorizedClientManager.setAuthorizedClientProvider(authorizedClientProvider);

        return authorizedClientManager;
    }

    @Bean
    public OAuth2AccessToken getOAuth2AccessToken(
            AuthorizedClientServiceOAuth2AuthorizedClientManager
                    authorizedClientServiceOAuth2AuthorizedClientManager) {

        return LiferayOAuth2Util.getOAuth2AccessToken(
                authorizedClientServiceOAuth2AuthorizedClientManager,
                _liferayOAuthApplicationExternalReferenceCodes);
    }

    @Bean
    WebClient webClient(@Value("${pulse.liferay.base-endpoint.scheme}") final String scheme,
                        @Value("${pulse.liferay.base-endpoint.host}") final String host,
                        @Value("${pulse.liferay.base-endpoint.port}") final Integer port,
                        final WebClient.Builder webClientBuilder,
                        final OAuth2AccessToken oAuth2AccessToken) throws MalformedURLException {

        _log.info(String.format("%s : %s", "pulse.liferay.base-endpoint.scheme", scheme));
        _log.info(String.format("%s : %s", "pulse.liferay.base-endpoint.host", host));
        _log.info(String.format("%s : %s", "pulse.liferay.base-endpoint.port", port));

        final URL baseUrl = new URL(scheme, host, port, "/");
        return webClientBuilder.baseUrl(baseUrl.toString()).defaultHeaders((h) -> h.setBearerAuth(oAuth2AccessToken.getTokenValue()))
                .build();
    }
}