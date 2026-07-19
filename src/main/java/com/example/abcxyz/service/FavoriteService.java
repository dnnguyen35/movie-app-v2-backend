package com.example.abcxyz.service;

import com.example.abcxyz.dto.request.FavoriteCreateRequest;
import com.example.abcxyz.dto.response.FavoriteResponse;

import java.util.List;

public interface FavoriteService {

    FavoriteResponse addFavorite(FavoriteCreateRequest favoriteCreateRequest);

    void removeFavorite(Long favoriteId);

    List<FavoriteResponse> getFavoritesOfUser();
}
