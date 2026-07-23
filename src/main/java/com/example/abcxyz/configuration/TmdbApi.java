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
public class TmdbApi {

    private final RestClient tmdbRestClient;
    private final TmdbEnpoints tmdbEnpoints;
    private final RedisTemplate<String, Object> redisTemplate;

    public TmdbApi(
            RestClient tmdbRestClient,
            TmdbEnpoints tmdbEnpoints,
            RedisTemplate<String, Object> redisTemplate
    ) {
        this.tmdbRestClient = tmdbRestClient;
        this.tmdbEnpoints = tmdbEnpoints;
        this.redisTemplate = redisTemplate;
    }

    public Object getMediaList(String mediaType, String mediaCategory, Integer page, String language) {

        String cachedDataKey = String.format(
                "tmdb:list:%s:%s:%d:%s",
                mediaType,
                mediaCategory,
                page,
                language
        );

        return this.getCachedDataFromRedisOrFetchDataFromTMDB(
                cachedDataKey,
                Duration.ofDays(1),
                () -> this.executeRestClientGetRequest(
                        this.tmdbEnpoints.mediaList(
                                mediaType,
                                mediaCategory,
                                page,
                                language
                        )
                )
        );
    }


    public Object getMediaDetail(String mediaType, Long mediaId, String language) {

        String cachedDataKey = String.format(
                "tmdb:detail:%s:%d:%s",
                mediaType,
                mediaId,
                language
        );

        return this.getCachedDataFromRedisOrFetchDataFromTMDB(
                cachedDataKey,
                Duration.ofDays(1),
                () -> this.executeRestClientGetRequest(
                        this.tmdbEnpoints.mediaDetail(
                                mediaType,
                                mediaId,
                                language
                        )
                )
        );
    }


    public Object getMediaGenres(String mediaType, String language) {

        String cachedDataKey = String.format(
                "tmdb:genres:%s:%s",
                mediaType,
                language
        );

        return this.getCachedDataFromRedisOrFetchDataFromTMDB(
                cachedDataKey,
                Duration.ofDays(1),
                () -> this.executeRestClientGetRequest(
                        this.tmdbEnpoints.mediaGenres(
                                mediaType,
                                language
                        )
                )
        );
    }


    public Object getMediaCredits(String mediaType, Long mediaId, String language) {

        String cachedDataKey = String.format(
                "tmdb:credits:%s:%d:%s",
                mediaType,
                mediaId,
                language
        );

        return this.getCachedDataFromRedisOrFetchDataFromTMDB(
                cachedDataKey,
                Duration.ofDays(1),
                () -> this.executeRestClientGetRequest(
                        this.tmdbEnpoints.mediaCredits(
                                mediaType,
                                mediaId,
                                language
                        )
                )
        );
    }


    public Object getMediaVideos(String mediaType, Long mediaId) {

        String cachedDataKey = String.format(
                "tmdb:videos:%s:%d",
                mediaType,
                mediaId
        );

        return this.getCachedDataFromRedisOrFetchDataFromTMDB(
                cachedDataKey,
                Duration.ofDays(1),
                () -> this.executeRestClientGetRequest(
                        this.tmdbEnpoints.mediaVideos(
                                mediaType,
                                mediaId
                        )
                )
        );
    }


    public Object getMediaRecommend(String mediaType, Long mediaId, String language) {

        String cachedDataKey = String.format(
                "tmdb:recommend:%s:%d:%s",
                mediaType,
                mediaId,
                language
        );

        return this.getCachedDataFromRedisOrFetchDataFromTMDB(
                cachedDataKey,
                Duration.ofDays(1),
                () -> this.executeRestClientGetRequest(
                        this.tmdbEnpoints.mediaRecommend(
                                mediaType,
                                mediaId,
                                language
                        )
                )
        );
    }


    public Object getMediaImages(String mediaType, Long mediaId, String language) {

        String cachedDataKey = String.format(
                "tmdb:images:%s:%d:%s",
                mediaType,
                mediaId,
                language
        );

        return this.getCachedDataFromRedisOrFetchDataFromTMDB(
                cachedDataKey,
                Duration.ofDays(1),
                () -> this.executeRestClientGetRequest(
                        this.tmdbEnpoints.mediaImages(
                                mediaType,
                                mediaId,
                                language
                        )
                )
        );
    }


    public Object getMediaSearch(String mediaType, String query, Integer page, String language) {

        String cachedDataKey = String.format(
                "tmdb:search:%s:%s:%d:%s",
                mediaType,
                query,
                page,
                language
        );

        return this.getCachedDataFromRedisOrFetchDataFromTMDB(
                cachedDataKey,
                Duration.ofDays(1),
                () -> this.executeRestClientGetRequest(
                        this.tmdbEnpoints.mediaSearch(
                                mediaType,
                                query,
                                page,
                                language
                        )
                )
        );
    }


    public Object getPersonDetail(Long personId, String language) {

        String cachedDataKey = String.format(
                "tmdb:person:detail:%d:%s",
                personId,
                language
        );

        return this.getCachedDataFromRedisOrFetchDataFromTMDB(
                cachedDataKey,
                Duration.ofDays(1),
                () -> this.executeRestClientGetRequest(
                        this.tmdbEnpoints.personDetail(
                                personId,
                                language
                        )
                )
        );
    }


    public Object getPersonMedias(Long personId, String language) {

        String cachedDataKey = String.format(
                "tmdb:person:medias:%d:%s",
                personId,
                language
        );

        return this.getCachedDataFromRedisOrFetchDataFromTMDB(
                cachedDataKey,
                Duration.ofDays(1),
                () -> this.executeRestClientGetRequest(
                        this.tmdbEnpoints.personMedias(
                                personId,
                                language
                        )
                )
        );
    }


    private Object executeRestClientGetRequest(URI getUri) {
        return this.tmdbRestClient
                .get()
                .uri(getUri)
                .retrieve()
                .body(Object.class);
    }

    private Object getCachedDataFromRedisOrFetchDataFromTMDB(String cachedDataKey, Duration cachedTime,
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