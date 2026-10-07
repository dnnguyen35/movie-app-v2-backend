package com.example.abcxyz.service;

public interface MediaService {

    Object getList(Integer page);

    Object getListByType(String type, Integer page);

    Object getDetail(String mediaSlug, String mediaGenre, String mediaId);

    Object getImages(String mediaSlug);

    Object getGenres();

    Object getListByGenre(String genreSlug, Integer page);

    Object getCountries();

    Object getListByCountry(String countrySlug, Integer page);

    Object getSearch(String keyword, Integer page);
}
