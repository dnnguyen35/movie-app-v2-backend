package com.example.abcxyz.configuration;

import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@Component
public class KkmovieEndpoints {

    public URI mediaList(Integer page) {
        return UriComponentsBuilder
                .fromPath("/danh-sach")
                .queryParamIfPresent("page", Optional.ofNullable(page))
                .build()
                .toUri();
    }

    public URI mediaListByType(String type, Integer page) {
        return UriComponentsBuilder
                .fromPath("/danh-sach/{type}")
                .queryParamIfPresent("page", Optional.ofNullable(page))
                .buildAndExpand(type)
                .toUri();
    }

    public URI mediaDetail(String mediaSlug) {
        return UriComponentsBuilder
                .fromPath("/phim/{mediaSlug}")
                .buildAndExpand(mediaSlug)
                .toUri();
    }

    public URI mediaImages(String mediaSlug) {
        return UriComponentsBuilder
                .fromPath("/phim/{mediaSlug}/images")
                .buildAndExpand(mediaSlug)
                .toUri();
    }

    public URI genres() {
        return UriComponentsBuilder
                .fromPath("/the-loai")
                .build()
                .toUri();
    }

    public URI mediaListByGenre(String genreSlug, Integer page) {
        return UriComponentsBuilder
                .fromPath("/the-loai/{genreSlug}")
                .queryParamIfPresent("page", Optional.ofNullable(page))
                .buildAndExpand(genreSlug)
                .toUri();
    }

    public URI countries() {
        return UriComponentsBuilder
                .fromPath("/quoc-gia")
                .build()
                .toUri();
    }

    public URI mediaListByCountry(String countrySlug, Integer page) {
        return UriComponentsBuilder
                .fromPath("/quoc-gia/{countrySlug}")
                .queryParamIfPresent("page", Optional.ofNullable(page))
                .buildAndExpand(countrySlug)
                .toUri();
    }

    public URI search(String keyword, Integer page) {
        return UriComponentsBuilder
                .fromPath("/tim-kiem")
                .queryParam("keyword", keyword)
                .queryParamIfPresent("page", Optional.ofNullable(page))
                .encode()
                .build()
                .toUri();
    }
}
