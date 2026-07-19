package com.example.abcxyz.mapper;

import com.example.abcxyz.dto.request.FavoriteCreateRequest;
import com.example.abcxyz.dto.response.FavoriteResponse;
import com.example.abcxyz.entity.Favorite;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FavoriteMapper {

    Favorite toFavorite(FavoriteCreateRequest favoriteCreateRequest);

    FavoriteResponse toFavoriteResponse(Favorite favorite);
}
