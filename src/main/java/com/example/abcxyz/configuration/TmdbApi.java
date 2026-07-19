package com.example.abcxyz.configuration;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.net.URI;

@Component
public class TmdbApi {

    private final RestClient tmdbRestClient;
    private final TmdbEnpoints tmdbEnpoints;

    public TmdbApi(RestClient tmdbRestClient, TmdbEnpoints tmdbEnpoints) {
        this.tmdbRestClient = tmdbRestClient;
        this.tmdbEnpoints = tmdbEnpoints;
    }

    public Object getMediaList(String mediaType, String mediaCategory, Integer page, String language) {
        return this.executeRestClientGetRequest(
                this.tmdbEnpoints.mediaList(mediaType, mediaCategory, page, language)
        );
    }

    public Object getMediaDetail(String mediaType, Long mediaId, String language) {
        return this.executeRestClientGetRequest(
                this.tmdbEnpoints.mediaDetail(mediaType, mediaId, language)
        );
    }

    public Object getMediaGenres(String mediaType, String language) {
        return this.executeRestClientGetRequest(
                this.tmdbEnpoints.mediaGenres(mediaType, language)
        );
    }

    public Object getMediaCredits(String mediaType, Long mediaId, String language) {
        return this.executeRestClientGetRequest(
                this.tmdbEnpoints.mediaCredits(mediaType, mediaId, language)
        );
    }

    public Object getMediaVideos(String mediaType, Long mediaId) {
        return this.executeRestClientGetRequest(
                this.tmdbEnpoints.mediaVideos(mediaType, mediaId)
        );
    }

    public Object getMediaRecommend(String mediaType, Long mediaId, String language) {
        return this.executeRestClientGetRequest(
                this.tmdbEnpoints.mediaRecommend(mediaType, mediaId, language)
        );
    }

    public Object getMediaImages(String mediaType, Long mediaId, String language) {
        return this.executeRestClientGetRequest(
                this.tmdbEnpoints.mediaImages(mediaType, mediaId, language)
        );
    }

    public Object getMediaSearch(String mediaType, String query, Integer page, String language) {
        return this.executeRestClientGetRequest(
                this.tmdbEnpoints.mediaSearch(mediaType, query, page, language)
        );
    }

    public Object getPersonDetail(Long personId, String language) {
        return this.executeRestClientGetRequest(
                this.tmdbEnpoints.personDetail(personId, language)
        );
    }

    public Object getPersonMedias(Long personId, String language) {
        return this.executeRestClientGetRequest(
                this.tmdbEnpoints.personMedias(personId, language)
        );
    }

    private Object executeRestClientGetRequest(URI getUri) {
        return this.tmdbRestClient
                .get()
                .uri(getUri)
                .retrieve()
                .body(Object.class);
    }
}
