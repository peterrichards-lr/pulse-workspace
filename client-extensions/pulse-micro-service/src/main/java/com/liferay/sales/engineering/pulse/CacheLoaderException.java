package com.liferay.sales.engineering.pulse;

public class CacheLoaderException extends PulseException {
    public CacheLoaderException(String message) {
        super(message);
    }

    public CacheLoaderException(String message, Exception innerException) {
        super(message, innerException);
    }
}
