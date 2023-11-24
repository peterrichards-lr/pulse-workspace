package com.liferay.sales.engineering.pulse.util;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Component
public class EnvUtils {
    private final Environment environment;
    private String port;
    private String hostname;

    public EnvUtils(final Environment environment) {
        this.environment = environment;
    }

    public String getPort() {
        if (port == null) port = environment.getProperty("local.server.port");
        return port;
    }

    public Integer getPortAsInt() {
        return Integer.valueOf(getPort());
    }

    public String getHostname() throws UnknownHostException {
        if (hostname == null) {
            if (InetAddress.getLocalHost().isLoopbackAddress()) {
                hostname = "localhost";
            } else {
                hostname = InetAddress.getLocalHost().getHostName();
            }
        }
        return hostname;
    }

    public String getServerUrlPrefix(String scheme) throws UnknownHostException {
        return String.format("%s://%s:%s", scheme,getHostname(),getPort());
    }
}
