package com.liferay.sales.engineering.pulse.service.liferay.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AcquisitionsResponse extends BasePageResponse<Acquisition> {
}
