package com.liferay.sales.engineering.pulse.rest;

public class DuplicateCampaignNameException extends RuntimeException {
    DuplicateCampaignNameException(String name) {
        super("There is an existing campaign named " + name);
    }
}
