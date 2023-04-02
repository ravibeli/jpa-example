package com.hr.app.jpaexample.config;

import com.github.benmanes.caffeine.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.github.benmanes.caffeine.cache.Caffeine;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheConfig {

    public static final long CACHE_MAXIMUM_SIZE = 10000L;
    public static Long CACHE_LIFE_TIME_IN_SECOND = 60L;

    public static final String EMPLOYEES_CACHE = "employees";

    public static final String EMPLOYEES_CACHE_MANAGER = "employees-cache-manager";

    @Bean(EMPLOYEES_CACHE_MANAGER)
    public CacheManager cacheManager() {
        CaffeineCacheManager caffeineCacheManager = new CaffeineCacheManager();
        caffeineCacheManager.registerCustomCache(EMPLOYEES_CACHE, createJpaExampleCache());
        return caffeineCacheManager;
    }

    @Bean
    public Cache<Object, Object> createJpaExampleCache() {
        return Caffeine.newBuilder()
                .expireAfterWrite(CACHE_LIFE_TIME_IN_SECOND, TimeUnit.SECONDS)
                .maximumSize(CACHE_MAXIMUM_SIZE)
                .recordStats()
                .build();
    }

}
