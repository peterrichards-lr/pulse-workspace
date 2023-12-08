package com.liferay.sales.engineering.pulse.rest.object.helper;

import com.liferay.sales.engineering.pulse.rest.object.model.ObjectAction;

public interface CampaignObjectActionHelper {
    void removeCampaign(final ObjectAction objectAction);

    void updateCampaign(final ObjectAction objectAction);
}
