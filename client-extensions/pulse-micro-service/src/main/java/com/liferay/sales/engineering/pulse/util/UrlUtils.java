package com.liferay.sales.engineering.pulse.util;

import java.net.MalformedURLException;
import java.net.URL;

public final class UrlUtils {

    public static URL buildUrlFromLiferayProperties(String serverProtocol, String mainDomain) throws MalformedURLException {
        return buildUrlFromLiferayProperties(serverProtocol, mainDomain, "");
    }

    public static URL buildUrlFromLiferayProperties(String serverProtocol, String mainDomain, String path) throws MalformedURLException {
        final String[] domain = mainDomain.split(":");
        if (domain.length == 1) {
            return new URL(serverProtocol, domain[0], path);
        } else {
            final int port = Integer.parseInt(domain[1]);
            return new URL(serverProtocol, domain[0], port, path);
        }
    }
}
