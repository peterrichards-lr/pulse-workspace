package com.liferay.sales.engineering.pulse.rest.api;

import com.liferay.sales.engineering.pulse.AsyncJobsManager;
import com.liferay.sales.engineering.pulse.rest.BaseRestController;
import com.liferay.sales.engineering.pulse.rest.DuplicateJobException;
import com.liferay.sales.engineering.pulse.rest.api.model.RefreshCacheJob;
import com.liferay.sales.engineering.pulse.service.CacheLoader;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/refresh-cache")
public class RefreshCacheController extends BaseRestController {
    private static final Log _log = LogFactory.getLog(
            RefreshCacheController.class);
    private final AsyncJobsManager asyncJobsManager;
    private final CacheLoader cacheLoader;

    public RefreshCacheController(final CacheLoader cacheLoader, final AsyncJobsManager asyncJobsManager) {
        this.cacheLoader = cacheLoader;
        this.asyncJobsManager = asyncJobsManager;
    }

    @GetMapping("/{job-id}")
    public ResponseEntity<RefreshCacheJob> getCacheRefreshStatus(
            @PathVariable(name = "job-id") final String jobId,
            @AuthenticationPrincipal final Jwt jwt) {
        if (!asyncJobsManager.hasJob(jobId)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        final CompletableFuture<?> cacheRefreshJob = asyncJobsManager.getJob(jobId);
        if (cacheRefreshJob.isDone())
            return new ResponseEntity<>(new RefreshCacheJob(jobId, RefreshCacheJob.JobStatus.COMPLETE), HttpStatus.OK);
        else if (cacheRefreshJob.isCompletedExceptionally())
            return new ResponseEntity<>(new RefreshCacheJob(jobId, RefreshCacheJob.JobStatus.EXCEPTION), HttpStatus.OK);
        else
            return new ResponseEntity<>(new RefreshCacheJob(jobId, RefreshCacheJob.JobStatus.SUBMITTED), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<RefreshCacheJob> requestCacheRefresh(
            @AuthenticationPrincipal final Jwt jwt) {
        final String jobId = UUID.randomUUID().toString();
        _log.info(String.format("Generated job-id %s for this request.", jobId));

        if (asyncJobsManager.hasJob(jobId)) {
            throw new DuplicateJobException("A job with same job-id already exists!");
        }

        final CompletableFuture<Void> cacheRefreshJob = cacheLoader.refreshCache();
        asyncJobsManager.putJob(jobId, cacheRefreshJob);
        _log.info(String.format("Job %s cache loader job submitted for processing", jobId));

        final RefreshCacheJob asyncApiResponse = new RefreshCacheJob(jobId, RefreshCacheJob.JobStatus.SUBMITTED);
        return new ResponseEntity<>(asyncApiResponse, HttpStatus.ACCEPTED);
    }
}
