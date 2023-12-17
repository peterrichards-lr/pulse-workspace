package com.liferay.sales.engineering.pulse.rest.object;


import com.liferay.sales.engineering.pulse.rest.BaseRestController;
import com.liferay.sales.engineering.pulse.rest.object.model.ObjectAction;
import org.apache.commons.logging.Log;
import org.json.JSONObject;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class BaseObjectActionController extends BaseRestController {
    protected static final String OBJECT_ENTRY_PROPERTY_PREFIX = "objectEntryDTO";
    private static final String OBJECT_ENTRY_PROPERTY_GROUP_NAME = "objectEntryPropertyName";
    private static final Pattern OBJECT_ENTRY_PROPERTY_PATTERN = Pattern.compile(String.format("%s(?<%s>[A-z\\d]+)", OBJECT_ENTRY_PROPERTY_PREFIX, OBJECT_ENTRY_PROPERTY_GROUP_NAME));

    protected String getObjectName(final String json, final Log log) {
        final JSONObject jsonObject = new JSONObject(json);
        for (final String propertyName : jsonObject.keySet()) {
            final Matcher matcher = OBJECT_ENTRY_PROPERTY_PATTERN.matcher(propertyName);
            final boolean matchFound = matcher.find();
            if (matchFound) {
                log.debug("Match found");
                return matcher.group(OBJECT_ENTRY_PROPERTY_GROUP_NAME);
            }
        }
        log.debug("Match not found");
        return null;
    }

    protected ObjectAction parseRequest(final Jwt jwt, final String json, final String objectEntryPropertyName, final Log log) {
        log(jwt, log, json);

        final JSONObject jsonObject = new JSONObject(json);
        final JSONObject objectEntry = jsonObject.getJSONObject(objectEntryPropertyName);

        final String objectName = objectEntryPropertyName.replaceAll(OBJECT_ENTRY_PROPERTY_PREFIX, "");

        final String trigger = jsonObject.has("objectActionTriggerKey") ? jsonObject.getString("objectActionTriggerKey") : null;
        final String erc = objectEntry.getString("externalReferenceCode");

        final JSONObject properties = objectEntry.getJSONObject("properties");

        if (log.isTraceEnabled()) {
            log.trace("Properties: " + properties.toString(4));
        }

        return new ObjectAction(objectName, trigger, erc, properties);
    }
}
