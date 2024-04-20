package com.nkd.eida_backend.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import jakarta.validation.constraints.NotNull;

import java.util.concurrent.TimeUnit;

public class CacheStore <K, V>{
    private final Cache<K, V> cache;

    public CacheStore(int expiryDuration, TimeUnit timeUnit){
        this.cache = CacheBuilder.newBuilder()
                .expireAfterWrite(expiryDuration, timeUnit)
                .concurrencyLevel(Runtime.getRuntime().availableProcessors())
                .build();
    }
    public V get(@NotNull K key){
        return this.cache.getIfPresent(key);
    }

    public void put(@NotNull K key, @NotNull V value){
        this.cache.put(key, value);
    }

    public void evict(@NotNull K key){
        this.cache.invalidate(key);
    }
}
