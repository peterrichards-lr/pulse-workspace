package com.liferay.sales.engineering.pulse.service.liferay;

import com.liferay.sales.engineering.pulse.service.liferay.model.Acquisition;

import java.net.URISyntaxException;
import java.util.List;

public interface LiferayAcquisitionService {
    List<Acquisition> getAcquisitions() throws URISyntaxException;
    Acquisition getByErc(String erc) throws URISyntaxException;
}
