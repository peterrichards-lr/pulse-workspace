package com.liferay.sales.engineering.pulse;

public class CacheLoaderException extends RuntimeException {
    public CacheLoaderException(String message) {
        super(message);
    }

    public CacheLoaderException(String message, Exception innerException) {
        super(message, innerException);
    }
}
