package com.nkd.eida_backend.cache;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class CacheConfig {

    @Bean
    public CacheStore<String, Integer> cacheStore(){
        return new CacheStore<>(15, TimeUnit.MINUTES);
    }
}
