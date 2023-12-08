package com.liferay.sales.engineering.pulse.rest;

import org.apache.commons.logging.Log;
import org.json.JSONObject;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Map;

public abstract class BaseRestController {
    protected void log(final Jwt jwt, final Log log) {
        if (log.isInfoEnabled()) {
            log.info("JWT Claims: " + jwt.getClaims());
            log.info("JWT ID: " + jwt.getId());
            log.info("JWT Subject: " + jwt.getSubject());
        }
    }

    protected void log(final Jwt jwt, final Log log, final Map<String, String> parameters) {
        if (log.isInfoEnabled()) {
            log.info("JWT Claims: " + jwt.getClaims());
            log.info("JWT ID: " + jwt.getId());
            log.info("JWT Subject: " + jwt.getSubject());
            log.info("Parameters: " + parameters);
        }
    }

    protected void log(final Jwt jwt, final Log log, final String json) {
        if (log.isInfoEnabled()) {
            try {
                JSONObject jsonObject = new JSONObject(json);

                log.info("JSON: " + jsonObject.toString(4));
            } catch (Exception exception) {
                log.error("JSON: " + json, exception);
            }

            log.info("JWT Claims: " + jwt.getClaims());
            log.info("JWT ID: " + jwt.getId());
            log.info("JWT Subject: " + jwt.getSubject());
        }
    }
}