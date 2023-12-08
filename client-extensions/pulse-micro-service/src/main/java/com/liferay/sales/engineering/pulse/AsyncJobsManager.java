package com.liferay.sales.engineering.pulse;

import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
public class AsyncJobsManager {

    private final ConcurrentMap<String, CompletableFuture<? extends Object>> mapOfJobs;

    public AsyncJobsManager() {
        mapOfJobs = new ConcurrentHashMap<>();
    }

    public CompletableFuture<? extends Object> getJob(String jobId) {
        return mapOfJobs.get(jobId);
    }

    public boolean hasJob(final String jobId) {
        return mapOfJobs.containsKey(jobId);
    }

    public void putJob(String jobId, CompletableFuture<? extends Object> theJob) {
        mapOfJobs.put(jobId, theJob);
    }

    public void removeJob(String jobId) {
        mapOfJobs.remove(jobId);
    }
}