package com.liferay.sales.engineering.pulse.service;

import java.util.concurrent.CompletableFuture;

public interface CacheLoader {
    CompletableFuture<Void> loadCache();

    CompletableFuture<Void> refreshCache();
}
