package com.example.abcxyz.service;

public interface MediaService {

    Object getList(String mediaType, String mediaCategory, Integer page, String language);

    Object getGenres(String mediaType, String language);

    Object getSearch(String mediaType, String query, Integer page, String language);

    Object getDetail(String mediaType, Long mediaId, String language);
}
