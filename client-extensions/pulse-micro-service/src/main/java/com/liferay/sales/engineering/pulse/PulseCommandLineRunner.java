package com.liferay.sales.engineering.pulse;

import com.liferay.sales.engineering.pulse.model.Status;
import com.liferay.sales.engineering.pulse.persistence.StatusRepository;
import com.liferay.sales.engineering.pulse.service.CacheLoader;
import com.liferay.sales.engineering.pulse.service.liferay.LiferayErrorResponseException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.security.oauth2.client.ClientAuthorizationException;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

@Component
public class PulseCommandLineRunner implements CommandLineRunner {
    private static final Log _log = LogFactory.getLog(
            PulseCommandLineRunner.class);
    private final AsyncJobsManager asyncJobsManager;
    private final boolean closeContextOnException;
    private final CacheLoader cacheLoader;
    private final ConfigurableApplicationContext context;
    private final StatusRepository statusRepository;

    public PulseCommandLineRunner(
            final ConfigurableApplicationContext context,
            final StatusRepository statusRepository,
            final CacheLoader cacheLoader,
            final AsyncJobsManager asyncJobsManager,
            @Value("${pulse.startup_exception_close_context}") final boolean closeContextOnException) {
        this.context = context;
        this.statusRepository = statusRepository;
        this.cacheLoader = cacheLoader;
        this.asyncJobsManager = asyncJobsManager;
        this.closeContextOnException = closeContextOnException;
    }

    private void populateStatus() {
        Arrays.asList("Draft", "Active", "Complete", "Inactive", "Expired").forEach((name) -> statusRepository.save(new Status(name)));
    }

    @Override
    public void run(String... params) {
        populateStatus();
        _log.info("Populated statuses");
        try {
            final String jobId = "start-up";
            CompletableFuture<Void> cacheLoaderTask = cacheLoader.loadCache();
            asyncJobsManager.putJob(jobId, cacheLoaderTask);
            CompletableFuture.completedFuture(cacheLoaderTask);
            _log.info("Start-up cache loader task submitted for processing");
        } catch (ClientAuthorizationException e) {
            _log.error("The OAuth 2 client credentials are invalid. Unable to load cache from Liferay");
            if (closeContextOnException)
                context.close();
        } catch (WebClientResponseException e) {
            _log.error("Unable to connect to Liferay", e);
            if (closeContextOnException)
                context.close();
        } catch (CacheLoaderException e) {
            _log.error("Unable to retrieve object data", e);
            if (closeContextOnException)
                context.close();
        } catch (LiferayErrorResponseException e) {
            _log.error(String.format("Unable to retrieve object data : %s", e));
            if (closeContextOnException)
                context.close();
        }
    }
}