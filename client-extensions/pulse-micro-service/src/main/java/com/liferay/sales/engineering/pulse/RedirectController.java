package com.liferay.sales.engineering.pulse;

import com.google.common.net.InternetDomainName;
import com.liferay.sales.engineering.pulse.model.Acquisition;
import com.liferay.sales.engineering.pulse.model.Campaign;
import com.liferay.sales.engineering.pulse.model.UrlToken;
import com.liferay.sales.engineering.pulse.persistence.UrlTokenRepository;
import com.liferay.sales.engineering.pulse.service.liferay.LiferayCampaignInteractionService;
import com.liferay.sales.engineering.pulse.service.liferay.model.Interaction;
import com.liferay.sales.engineering.pulse.util.EnvUtils;
import com.liferay.sales.engineering.pulse.util.HttpRequestResponseUtils;
import com.liferay.sales.engineering.pulse.util.StringUtils;
import com.liferay.sales.engineering.pulse.util.UrlUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
public class RedirectController {
    private static final Log _log = LogFactory.getLog(
            RedirectController.class);
    private final URL baseUrl;
    private final String cookieDomain;
    private final EnvUtils envUtil;
    private final LiferayCampaignInteractionService liferayCampaignInteractionService;
    private final boolean testRedirect;
    private final UrlTokenRepository tokenRepository;

    @Autowired
    public RedirectController(
            @Value("${com.liferay.lxc.dxp.server.protocol}") final String lxcServerProtocol,
            @Value("${com.liferay.lxc.dxp.main.domain}") final String lxcMainDomain,
            @Value("${pulse.cookie_domain}") final String cookieDomain,
            @Value("${pulse.test_redirect}") final boolean testRedirect,
            final EnvUtils envUtil,
            final UrlTokenRepository tokenRepository,
            final LiferayCampaignInteractionService liferayCampaignInteractionService) throws MalformedURLException {
        _log.debug(String.format("%s : %s", "com.liferay.lxc.dxp.server.protocol", lxcServerProtocol));
        _log.debug(String.format("%s : %s", "com.liferay.lxc.dxp.main.domain", lxcMainDomain));
        this.baseUrl = UrlUtils.buildUrlFromLiferayProperties(lxcServerProtocol, lxcMainDomain);

        _log.info(String.format("%s : %s", "baseUrl.getHost()", baseUrl.getHost()));
        _log.info(String.format("%s : %s", "baseUrl.getPort()", baseUrl.getPort()));
        _log.debug(String.format("%s : %s", "baseUrl", this.baseUrl));
        this.cookieDomain = cookieDomain;
        this.testRedirect = testRedirect;
        this.envUtil = envUtil;
        this.tokenRepository = tokenRepository;
        this.liferayCampaignInteractionService = liferayCampaignInteractionService;
    }

    private void addCookies(final Map<String, String> cookieMap,
                            final InternetDomainName hostDomainName,
                            final HttpServletResponse httpServletResponse) {
        cookieMap.keySet().stream().map((key) -> {
            final String value = cookieMap.get(key);
            final Cookie cookie = new Cookie(key, value);
            cookie.setHttpOnly(false);
            cookie.setSecure(true);
            if (!StringUtils.isBlank(cookieDomain)) {
                cookie.setDomain(cookieDomain);
            } else if (hostDomainName != null && hostDomainName.isUnderRegistrySuffix() &&
                    StringUtils.isNotBlank(Objects.requireNonNull(hostDomainName.publicSuffix()).toString())) {
                cookie.setDomain(hostDomainName.publicSuffix().toString());
            }
            _log.info(String.format("Cookie domain : %s", cookie.getDomain()));
            return cookie;
        }).forEach(httpServletResponse::addCookie);
    }

    private URL buildUrl(final Campaign campaign,
                         final Acquisition acquisition) throws MalformedURLException, UnknownHostException {
        final String targetUrl = campaign.getCampaignUrl();
        final String campaignUrl;
        if (targetUrl.matches("^https?://")) {
            campaignUrl = targetUrl;
        } else if (this.testRedirect) {
            campaignUrl = this.envUtil.getServerUrlPrefix(this.baseUrl.getProtocol()) + targetUrl;
        } else if (targetUrl.startsWith("/")) {
            campaignUrl = this.baseUrl + targetUrl;
        } else {
            campaignUrl = this.baseUrl + "/" + targetUrl;
        }

        _log.info(String.format("campaignUrl : %s", campaignUrl));

        StringBuilder url = new StringBuilder(campaignUrl);
        url.append("?");
        if (StringUtils.isNotBlank(campaign.getName())) {
            url.append("utm_campaign=");
            url.append(campaign.getName());
        }

        if (acquisition == null) return new URL(url.toString());

        if (StringUtils.isNotBlank(acquisition.getContent())) {
            if (url.length() > 1) {
                url.append("&");
            }
            url.append("utm_content=");
            url.append(acquisition.getContent());
        }
        if (StringUtils.isNotBlank(acquisition.getMedium())) {
            if (url.length() > 1) {
                url.append("&");
            }
            url.append("utm_medium=");
            url.append(acquisition.getMedium());
        }
        if (StringUtils.isNotBlank(acquisition.getSource())) {
            if (url.length() > 1) {
                url.append("&");
            }
            url.append("utm_source=");
            url.append(acquisition.getSource());
        }
        if (StringUtils.isNotBlank(acquisition.getTerm())) {
            if (url.length() > 1) {
                url.append("&");
            }
            url.append("utm_term=");
            url.append(acquisition.getTerm());
        }
        return new URL(url.toString());
    }

    private void configureRedirection(final Campaign campaign,
                                      final Acquisition acquisition,
                                      final String urlToken,
                                      final Long interactionId,
                                      final InternetDomainName hostDomainName,
                                      final HttpServletResponse httpServletResponse) throws MalformedURLException, UnknownHostException {
        final String redirectionUrl = buildUrl(campaign, acquisition).toString();
        httpServletResponse.setHeader("Location", redirectionUrl);

        final Map<String, String> cookies = new HashMap<>() {{
            put("__pcId", String.valueOf(campaign.getId()));
            put("__pcUt", urlToken);
            put("__intId", String.valueOf(interactionId));
        }};

        addCookies(cookies, hostDomainName, httpServletResponse);
        httpServletResponse.setStatus(302);
    }

    private boolean isCampaignActive(final Campaign campaign,
                                     final LocalDateTime interactionTime) {
        final String status = campaign.getStatus().getName();
        return status.equals("Active") && (campaign.getEnd() == null || campaign.getEnd().isAfter(interactionTime));
    }

    private boolean isCampaignDraft(final Campaign campaign) {
        final String status = campaign.getStatus().getName();
        return status.equals("Draft");
    }

    private Long recordInteraction(final UrlToken urlToken,
                                   final LocalDateTime interactionTime,
                                   final HttpServletRequest httpServletRequest) {
        final String userAgent = httpServletRequest.getHeader("User-Agent");
        final String ipAddress = HttpRequestResponseUtils.getClientIpAddressIfServletRequestExist();
        try {
            final Interaction interaction = liferayCampaignInteractionService.createInteraction("Pulse redirect", urlToken, interactionTime, userAgent, ipAddress);
            if (interaction == null)
                return -1L;

            return interaction.getId();
        } catch (URISyntaxException e) {
            _log.error("Unable to record campaign interaction", e);
            return -1L;
        }
    }

    @RequestMapping(value = "/redirect")
    public ResponseEntity<String> redirect(final HttpServletRequest httpServletRequest) {
        _log.info("****** RedirectController:: redirect ******");
        _log.info(httpServletRequest.getQueryString());
        Collections.list(httpServletRequest.getHeaderNames()).forEach((headerName -> _log.info(String.format("%s : %s", headerName, httpServletRequest.getHeader(headerName)))));
        _log.info("*******************************************");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(new MediaType("text", "plain", StandardCharsets.UTF_8));
        return new ResponseEntity<>("Redirect OK", httpHeaders, HttpStatus.OK);
    }

    @RequestMapping(value = {"/c/{urlToken:[A-z\\d]{8}}"})
    public void redirect(@RequestHeader final String host,
                         @PathVariable final String urlToken,
                         final HttpServletRequest httpServletRequest,
                         final HttpServletResponse httpServletResponse) throws MalformedURLException, UnknownHostException {
        _log.info(String.format("host : %s", host));
        InternetDomainName hostDomainName = null;
        try {
            Pattern pattern = Pattern.compile("^([^#]*?):?\\d*?$");
            Matcher matcher = pattern.matcher(host);
            if (matcher.find()) {
                hostDomainName = InternetDomainName.from(matcher.group(1));
            }
        } catch (IllegalArgumentException e) {
            _log.warn(e.getMessage());
        }

        final LocalDateTime interactionTime = LocalDateTime.now(ZoneId.of("UTC"));
        final Optional<UrlToken> optionalToken = tokenRepository.findById(urlToken);

        if (optionalToken.isEmpty()) {
            _log.info(String.format("No token found for : %s", urlToken));
            httpServletResponse.setStatus(404);
            return;
        }

        final UrlToken token = optionalToken.get();
        _log.info(String.format("token : %s", token));

        final Campaign campaign = token.getCampaign();

        if (isCampaignDraft(campaign)) {
            httpServletResponse.setStatus(423);
            return;
        }

        if (!isCampaignActive(campaign, interactionTime)) {
            httpServletResponse.setStatus(410);
            return;
        }

        final Long interactionId = recordInteraction(token, interactionTime, httpServletRequest);
        final Acquisition acquisition = token.getAcquisition();

        configureRedirection(campaign, acquisition, urlToken, interactionId, hostDomainName, httpServletResponse);
        _log.info(String.format("Redirecting to %s", httpServletResponse.getHeader("Location")));
    }
}
