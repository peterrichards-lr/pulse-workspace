package com.liferay.sales.engineering.pulse.service.liferay.model;

import java.util.List;

public abstract class BasePageResponse<T> {
    List<T> items;
    long page;
    long pageSize;
    long totalSize;

    public List<T> getItems() {
        return items;
    }

    public void setItems(final List<T> items) {
        this.items = items;
    }

    public long getPage() {
        return page;
    }

    public void setPage(final long page) {
        this.page = page;
    }

    public long getPageSize() {
        return pageSize;
    }

    public void setPageSize(final long pageSize) {
        this.pageSize = pageSize;
    }

    public long getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(final long totalSize) {
        this.totalSize = totalSize;
    }

    @Override
    public String toString() {
        return "CampaignsResponse{" +
                "items=" + items +
                ", page=" + page +
                ", pageSize=" + pageSize +
                ", totalSize=" + totalSize +
                '}';
    }
}
