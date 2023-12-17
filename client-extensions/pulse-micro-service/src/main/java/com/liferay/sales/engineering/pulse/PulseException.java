package com.liferay.sales.engineering.pulse;

public class PulseException extends RuntimeException {
    public PulseException(String message) {
        super(message);
    }

    public PulseException(String message, Exception innerException) {
        super(message, innerException);
    }
}
