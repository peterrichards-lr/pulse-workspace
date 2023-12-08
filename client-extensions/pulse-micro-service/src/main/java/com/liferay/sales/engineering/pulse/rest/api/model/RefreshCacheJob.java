package com.liferay.sales.engineering.pulse.rest.api.model;

import com.google.common.base.Objects;

public class RefreshCacheJob {
    private final String jobId;
    private final JobStatus status;

    public RefreshCacheJob(final String jobId, final JobStatus status) {
        this.jobId = jobId;
        this.status = status;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof RefreshCacheJob)) return false;
        final RefreshCacheJob that = (RefreshCacheJob) o;
        return Objects.equal(jobId, that.jobId) && Objects.equal(status, that.status);
    }

    public String getJobId() {
        return jobId;
    }

    public JobStatus getStatus() {
        return status;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(jobId, status);
    }

    @Override
    public String toString() {
        return "RefreshCacheJob{" +
                "jobId='" + jobId + '\'' +
                ", status='" + status + '\'' +
                '}';
    }

    public enum JobStatus {
        SUBMITTED,
        COMPLETE,
        EXCEPTION
    }
}
