package com.liferay.sales.engineering.pulse.rest;

public class DuplicateJobException extends RuntimeException {
    public DuplicateJobException(final String message) {
        super(message);
    }
}
