package com.example.abcxyz.configuration;

import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@Component
public class TmdbEnpoints {

    public URI mediaList(String mediaType, String mediaCategory, Integer page, String language) {
        return UriComponentsBuilder
                .fromPath(mediaType.toLowerCase() + "/" + mediaCategory)
                .queryParam("language", this.getLanguageMode(language))
                .queryParamIfPresent("page", Optional.ofNullable(page))
                .build()
                .toUri();
    }

    public URI mediaDetail(String mediaType, Long mediaId, String language) {
        return UriComponentsBuilder
                .fromPath(mediaType.toLowerCase() + "/" + mediaId)
                .queryParam("language", this.getLanguageMode(language))
                .build()
                .toUri();
    }

    public URI mediaGenres(String mediaType, String language) {
        return UriComponentsBuilder
                .fromPath("genre" + "/" + mediaType.toLowerCase() + "/" + "list")
                .queryParam("language", this.getLanguageMode(language))
                .build()
                .toUri();
    }

    public URI mediaCredits(String mediaType, Long mediaId, String language) {
        return UriComponentsBuilder
                .fromPath(mediaType.toLowerCase() + "/" + mediaId + "/" + "credits")
                .queryParam("language", this.getLanguageMode(language))
                .build()
                .toUri();
    }

    public URI mediaVideos(String mediaType, Long mediaId) {
        return UriComponentsBuilder
                .fromPath(mediaType.toLowerCase() + "/" + mediaId + "/" + "videos")
                .queryParam("language", "en-US")
                .build()
                .toUri();
    }

    public URI mediaRecommend(String mediaType, Long mediaId, String language) {
        return UriComponentsBuilder
                .fromPath(mediaType.toLowerCase() + "/" + mediaId + "/" + "recommendations")
                .queryParam("language", this.getLanguageMode(language))
                .build()
                .toUri();
    }

    public URI mediaImages(String mediaType, Long mediaId, String language) {
        String includeImageLanguage = "en".equalsIgnoreCase(language) ? "en,null" : "vi,null";

        return UriComponentsBuilder
                .fromPath(mediaType.toLowerCase() + "/" + mediaId + "/" + "images")
                .queryParam("language", this.getLanguageMode(language))
                .queryParam("include_image_language", includeImageLanguage)
                .build()
                .toUri();
    }

    public URI mediaSearch(String mediaType, String query, Integer page, String language) {
        return UriComponentsBuilder
                .fromPath("search" + "/" + mediaType.toLowerCase())
                .queryParam("query", query)
                .queryParam("language", this.getLanguageMode(language))
                .queryParamIfPresent("page", Optional.ofNullable(page))
                .build()
                .toUri();
    }

    public URI personDetail(Long personId, String language) {
        return UriComponentsBuilder
                .fromPath("person" + "/" + personId)
                .queryParam("language", this.getLanguageMode(language))
                .build()
                .toUri();
    }

    public URI personMedias(Long personId, String language) {
        return UriComponentsBuilder
                .fromPath("person" + "/" + personId + "/" + "combined_credits")
                .queryParam("language", this.getLanguageMode(language))
                .build()
                .toUri();
    }

    private String getLanguageMode(String language) {
        return "en".equalsIgnoreCase(language) ? "en-US" : "vi-VN";
    }
}
