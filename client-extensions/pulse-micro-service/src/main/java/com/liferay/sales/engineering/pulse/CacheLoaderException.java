package com.liferay.sales.engineering.pulse;

public class CacheLoaderException extends RuntimeException {
    CacheLoaderException(String message) {
        super(message);
    }

    CacheLoaderException(String message, Exception innerException) {
        super(message, innerException);
    }
}
