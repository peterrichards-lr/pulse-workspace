package com.liferay.sales.engineering.pulse;

public class DuplicateCampaignNameException extends RuntimeException {
    public DuplicateCampaignNameException(String name) {
        super("There is an existing campaign named " + name);
    }
}
