package com.liferay.sales.engineering.pulse.rest.object.model;

import com.google.common.base.Objects;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ObjectAction {
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    private final String erc;
    private final String objectName;
    private final JSONObject properties;
    private final String trigger;

    public ObjectAction(final String objectName, final String trigger, final String erc, final JSONObject properties) {
        this.objectName = objectName;
        this.trigger = trigger;
        this.erc = erc;
        this.properties = properties;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof ObjectAction)) return false;
        final ObjectAction that = (ObjectAction) o;
        return Objects.equal(erc, that.erc) && Objects.equal(properties, that.properties) && Objects.equal(trigger, that.trigger) && Objects.equal(objectName, that.objectName);
    }

    public String getErc() {
        return erc;
    }

    public LocalDateTime getLocalDateTime(final String key) {
        return has(key) ? LocalDateTime.parse(getString(key), dateTimeFormatter) : null;
    }

    public Map<String, Object> getMap(final String key) {
        final Map<String, Object> map = new HashMap<>();
        if (!has(key))
            return map;
        final JSONObject jsonObject = properties.getJSONObject(key);
        final Iterator<String> keys = jsonObject.keys();
        while (keys.hasNext()) {
            final String iteratorKey = keys.next();
            final Object value = jsonObject.get(iteratorKey);
            map.put(iteratorKey, value);
        }
        return map;
    }

    public String getObjectName() {
        return objectName;
    }

    public String getString(final String key) {
        return properties.getString(key);
    }

    public boolean has(final String key) {
        return properties.has(key);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(erc, properties, trigger, objectName);
    }

    public boolean isDelete() {
        return trigger != null && trigger.contains("Delete");
    }

    public boolean isUpdate() {
        return trigger != null && trigger.contains("Update");
    }

    @Override
    public String toString() {
        return "ObjectAction{" +
                "erc='" + erc + '\'' +
                ", properties=" + properties +
                ", trigger='" + trigger + '\'' +
                ", objectName='" + objectName + '\'' +
                '}';
    }
}
