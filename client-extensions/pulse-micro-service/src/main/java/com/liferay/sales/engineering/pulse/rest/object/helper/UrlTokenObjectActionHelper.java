package com.liferay.sales.engineering.pulse.rest.object.helper;

import com.liferay.sales.engineering.pulse.rest.object.model.ObjectAction;

public interface UrlTokenObjectActionHelper {
    void removeUrlToken(final ObjectAction objectAction);

    void updateUrlToken(final ObjectAction objectAction);
}
