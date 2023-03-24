package com.example.driveronboardingservice.service;

import com.example.driveronboardingservice.dto.DriverDetailsDto;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class CacheService {
    Cache<String, DriverDetailsDto> cache = CacheBuilder.newBuilder()
            .maximumSize(100)
            .expireAfterAccess(10, TimeUnit.MINUTES)
            .build();

    /**
     * Function to add key-value to cache.
     * @param key
     * @param value
     */
    public void put(String key, DriverDetailsDto value) {
        log.info("Adding the entry in cache for user: {}", key);
        cache.put(key, value);
    }

    /**
     * Function to get key-value from cache if present.
     * @param key
     * @return
     */
    public DriverDetailsDto get(String key) {
        log.info("Getting the value for user from cache: {}", key);
        return cache.getIfPresent(key);
    }

    /**
     * Function to delete key-value from cache.
     * @param key
     */
    public void delete(String key) {
        log.info("Deleting the value for user from cache: {}", key);
        cache.invalidate(key);
    }

}
