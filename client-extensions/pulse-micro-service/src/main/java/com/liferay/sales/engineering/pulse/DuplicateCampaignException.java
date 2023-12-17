package com.liferay.sales.engineering.pulse;

public class DuplicateCampaignException extends PulseException {
    public DuplicateCampaignException(String name) {
        super("There is an existing campaign named " + name);
    }
}
