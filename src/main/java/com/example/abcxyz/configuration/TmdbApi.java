package com.example.abcxyz.configuration;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.net.URI;
import java.util.concurrent.TimeUnit;

@Component
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

        String key = String.format(
                "tmdb:list:%s:%s:%d:%s",
                mediaType,
                mediaCategory,
                page,
                language
        );

        Object cachedData = this.redisTemplate.opsForValue().get(key);

        if (cachedData != null) {
            return cachedData;
        }

        Object data = this.executeRestClientGetRequest(
                this.tmdbEnpoints.mediaList(mediaType, mediaCategory, page, language)
        );

        this.redisTemplate.opsForValue().set(
                key,
                data,
                86400,
                TimeUnit.SECONDS
        );

        return data;
    }


    public Object getMediaDetail(String mediaType, Long mediaId, String language) {

        String key = String.format(
                "tmdb:detail:%s:%d:%s",
                mediaType,
                mediaId,
                language
        );

        Object cachedData = this.redisTemplate.opsForValue().get(key);

        if (cachedData != null) {
            return cachedData;
        }

        Object data = this.executeRestClientGetRequest(
                this.tmdbEnpoints.mediaDetail(mediaType, mediaId, language)
        );

        this.redisTemplate.opsForValue().set(
                key,
                data,
                86400,
                TimeUnit.SECONDS
        );

        return data;
    }


    public Object getMediaGenres(String mediaType, String language) {

        String key = String.format(
                "tmdb:genres:%s:%s",
                mediaType,
                language
        );

        Object cachedData = this.redisTemplate.opsForValue().get(key);

        if (cachedData != null) {
            return cachedData;
        }

        Object data = this.executeRestClientGetRequest(
                this.tmdbEnpoints.mediaGenres(mediaType, language)
        );

        this.redisTemplate.opsForValue().set(
                key,
                data,
                86400,
                TimeUnit.SECONDS
        );

        return data;
    }


    public Object getMediaCredits(String mediaType, Long mediaId, String language) {

        String key = String.format(
                "tmdb:credits:%s:%d:%s",
                mediaType,
                mediaId,
                language
        );

        Object cachedData = this.redisTemplate.opsForValue().get(key);

        if (cachedData != null) {
            return cachedData;
        }

        Object data = this.executeRestClientGetRequest(
                this.tmdbEnpoints.mediaCredits(mediaType, mediaId, language)
        );

        this.redisTemplate.opsForValue().set(
                key,
                data,
                86400,
                TimeUnit.SECONDS
        );

        return data;
    }


    public Object getMediaVideos(String mediaType, Long mediaId) {

        String key = String.format(
                "tmdb:videos:%s:%d",
                mediaType,
                mediaId
        );

        Object cachedData = this.redisTemplate.opsForValue().get(key);

        if (cachedData != null) {
            return cachedData;
        }

        Object data = this.executeRestClientGetRequest(
                this.tmdbEnpoints.mediaVideos(mediaType, mediaId)
        );

        this.redisTemplate.opsForValue().set(
                key,
                data,
                86400,
                TimeUnit.SECONDS
        );

        return data;
    }


    public Object getMediaRecommend(String mediaType, Long mediaId, String language) {

        String key = String.format(
                "tmdb:recommend:%s:%d:%s",
                mediaType,
                mediaId,
                language
        );

        Object cachedData = this.redisTemplate.opsForValue().get(key);

        if (cachedData != null) {
            return cachedData;
        }

        Object data = this.executeRestClientGetRequest(
                this.tmdbEnpoints.mediaRecommend(mediaType, mediaId, language)
        );

        this.redisTemplate.opsForValue().set(
                key,
                data,
                86400,
                TimeUnit.SECONDS
        );

        return data;
    }


    public Object getMediaImages(String mediaType, Long mediaId, String language) {

        String key = String.format(
                "tmdb:images:%s:%d:%s",
                mediaType,
                mediaId,
                language
        );

        Object cachedData = this.redisTemplate.opsForValue().get(key);

        if (cachedData != null) {
            return cachedData;
        }

        Object data = this.executeRestClientGetRequest(
                this.tmdbEnpoints.mediaImages(mediaType, mediaId, language)
        );

        this.redisTemplate.opsForValue().set(
                key,
                data,
                86400,
                TimeUnit.SECONDS
        );

        return data;
    }


    public Object getMediaSearch(String mediaType, String query, Integer page, String language) {

        String key = String.format(
                "tmdb:search:%s:%s:%d:%s",
                mediaType,
                query,
                page,
                language
        );

        Object cachedData = this.redisTemplate.opsForValue().get(key);

        if (cachedData != null) {
            return cachedData;
        }

        Object data = this.executeRestClientGetRequest(
                this.tmdbEnpoints.mediaSearch(mediaType, query, page, language)
        );

        this.redisTemplate.opsForValue().set(
                key,
                data,
                600,
                TimeUnit.SECONDS
        );

        return data;
    }


    public Object getPersonDetail(Long personId, String language) {

        String key = String.format(
                "tmdb:person:detail:%d:%s",
                personId,
                language
        );

        Object cachedData = this.redisTemplate.opsForValue().get(key);

        if (cachedData != null) {
            return cachedData;
        }

        Object data = this.executeRestClientGetRequest(
                this.tmdbEnpoints.personDetail(personId, language)
        );

        this.redisTemplate.opsForValue().set(
                key,
                data,
                86400,
                TimeUnit.SECONDS
        );

        return data;
    }


    public Object getPersonMedias(Long personId, String language) {

        String key = String.format(
                "tmdb:person:medias:%d:%s",
                personId,
                language
        );

        Object cachedData = this.redisTemplate.opsForValue().get(key);

        if (cachedData != null) {
            return cachedData;
        }

        Object data = this.executeRestClientGetRequest(
                this.tmdbEnpoints.personMedias(personId, language)
        );

        this.redisTemplate.opsForValue().set(
                key,
                data,
                86400,
                TimeUnit.SECONDS
        );

        return data;
    }


    private Object executeRestClientGetRequest(URI getUri) {
        return this.tmdbRestClient
                .get()
                .uri(getUri)
                .retrieve()
                .body(Object.class);
    }
}