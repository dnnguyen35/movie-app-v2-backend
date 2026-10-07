package com.example.abcxyz.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.net.URI;
import java.time.Duration;
import java.util.function.Supplier;

@Component
@Slf4j
public class KkmovieApi {

    private final RestClient kkmovieRestClient;
    private final KkmovieEndpoints kkmovieEndpoints;
    private final RedisTemplate<String, Object> redisTemplate;

    public KkmovieApi(RestClient kkmovieRestClient, KkmovieEndpoints kkmovieEndpoints,
                      RedisTemplate<String, Object> redisTemplate) {
        this.kkmovieRestClient = kkmovieRestClient;
        this.kkmovieEndpoints = kkmovieEndpoints;
        this.redisTemplate = redisTemplate;
    }

    public Object getMediaList(Integer page) {
        String cachedDataKey = String.format("kkmovie:list:%d", page);

        return this.getCachedDataFromRedisOrFetchDataFromKkmovie(
                cachedDataKey,
                Duration.ofDays(1),
                () -> this.executeRestClientGetRequest(
                        this.kkmovieEndpoints.mediaList(page)
                )
        );
    }

    public Object getMediaListByType(String type, Integer page) {
        String cachedDataKey = String.format("kkmovie:list:%s:%d", type, page);

        return this.getCachedDataFromRedisOrFetchDataFromKkmovie(
                cachedDataKey,
                Duration.ofDays(1),
                () -> this.executeRestClientGetRequest(
                        this.kkmovieEndpoints.mediaListByType(type, page)
                )
        );
    }

    public Object getMediaDetail(String mediaSlug) {
        String cachedDataKey = String.format("kkmovie:detail:%s", mediaSlug);

        return this.getCachedDataFromRedisOrFetchDataFromKkmovie(
                cachedDataKey,
                Duration.ofDays(1),
                () -> this.executeRestClientGetRequest(
                        this.kkmovieEndpoints.mediaDetail(mediaSlug)
                )
        );
    }

    public Object getMediaImages(String mediaSlug) {
        String cachedDataKey = String.format("kkmovie:images:%s", mediaSlug);

        return this.getCachedDataFromRedisOrFetchDataFromKkmovie(
                cachedDataKey,
                Duration.ofDays(1),
                () -> this.executeRestClientGetRequest(
                        this.kkmovieEndpoints.mediaImages(mediaSlug)
                )
        );
    }

    public Object getGenres() {
        String cachedDataKey = "kkmovie:genres";

        return this.getCachedDataFromRedisOrFetchDataFromKkmovie(
                cachedDataKey,
                Duration.ofDays(1),
                () -> this.executeRestClientGetRequest(
                        this.kkmovieEndpoints.genres()
                )
        );
    }

    public Object getMediaListByGenre(String genreSlug, Integer page) {
        String cachedDataKey = String.format("kkmovie:list:genre:%s:%d", genreSlug, page);

        return this.getCachedDataFromRedisOrFetchDataFromKkmovie(
                cachedDataKey,
                Duration.ofDays(1),
                () -> this.executeRestClientGetRequest(
                        this.kkmovieEndpoints.mediaListByGenre(genreSlug, page)
                )
        );
    }

    public Object getCountries() {
        String cachedDataKey = "kkmovie:countries";

        return this.getCachedDataFromRedisOrFetchDataFromKkmovie(
                cachedDataKey,
                Duration.ofDays(1),
                () -> this.executeRestClientGetRequest(
                        this.kkmovieEndpoints.countries()
                )
        );
    }

    public Object getMediaListByCountry(String countrySlug, Integer page) {
        String cachedDataKey = String.format("kkmovie:list:country:%s:%d", countrySlug, page);

        return this.getCachedDataFromRedisOrFetchDataFromKkmovie(
                cachedDataKey,
                Duration.ofDays(1),
                () -> this.executeRestClientGetRequest(
                        this.kkmovieEndpoints.mediaListByCountry(countrySlug, page)
                )
        );
    }

    public Object getSearch(String keyword, Integer page) {
        String cachedDataKey = String.format("kkmovie:search:%s:%d", keyword, page);

        return this.getCachedDataFromRedisOrFetchDataFromKkmovie(
                cachedDataKey,
                Duration.ofDays(1),
                () -> this.executeRestClientGetRequest(
                        this.kkmovieEndpoints.search(keyword, page)
                )
        );
    }

    private Object executeRestClientGetRequest(URI getUri) {
        log.info("Kkmovie URI: {}", getUri);

        return this.kkmovieRestClient
                .get()
                .uri(getUri)
                .retrieve()
                .body(Object.class);
    }

    private Object getCachedDataFromRedisOrFetchDataFromKkmovie(String cachedDataKey, Duration cachedTime,
                                                                Supplier<Object> supplier) {
        Object cachedData = null;

        try {
            cachedData = this.redisTemplate.opsForValue().get(cachedDataKey);
        } catch (Exception ex) {
            log.error("Get redis cached data failed for key: {}", cachedDataKey, ex);
        }

        if (cachedData != null) {
            return cachedData;
        }

        Object data = supplier.get();

        try {
            this.redisTemplate.opsForValue().set(cachedDataKey, data, cachedTime);
        } catch (Exception ex) {
            log.error("Set redis cached data failed for key: {}", cachedDataKey, ex);
        }

        return data;
    }
}
