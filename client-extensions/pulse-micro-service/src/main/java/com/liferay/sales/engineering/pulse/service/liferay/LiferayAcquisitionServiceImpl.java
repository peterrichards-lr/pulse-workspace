package com.liferay.sales.engineering.pulse.service.liferay;

import com.liferay.sales.engineering.pulse.service.liferay.model.AcquisitionsResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class LiferayAcquisitionServiceImpl implements LiferayAcquisitionService {
    private static final Log _log = LogFactory.getLog(
            LiferayAcquisitionServiceImpl.class);

    private final WebClient webClient;

    public LiferayAcquisitionServiceImpl(final WebClient webClient) {
        this.webClient = webClient;
    }

    public List<Object> getAcquisitions() {
        Mono<AcquisitionsResponse> campaignResponse = this.webClient.get().uri("/o/c/acquisitions/")
                .retrieve().bodyToMono(new ParameterizedTypeReference<>() {
                });

        return campaignResponse.block().getItems();
    }
}
