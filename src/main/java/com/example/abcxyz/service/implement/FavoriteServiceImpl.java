package com.example.abcxyz.service.implement;

import com.example.abcxyz.dto.request.FavoriteCreateRequest;
import com.example.abcxyz.dto.response.FavoriteResponse;
import com.example.abcxyz.entity.Favorite;
import com.example.abcxyz.entity.User;
import com.example.abcxyz.enums.ErrorCode;
import com.example.abcxyz.exception.AppException;
import com.example.abcxyz.mapper.FavoriteMapper;
import com.example.abcxyz.repository.FavoriteRepository;
import com.example.abcxyz.repository.UserRepository;
import com.example.abcxyz.service.FavoriteService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FavoriteServiceImpl implements FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final FavoriteMapper favoriteMapper;
    private final UserRepository userRepository;

    public FavoriteServiceImpl(
            FavoriteRepository favoriteRepository,
            FavoriteMapper favoriteMapper,
            UserRepository userRepository
    ) {
        this.favoriteRepository = favoriteRepository;
        this.favoriteMapper = favoriteMapper;
        this.userRepository = userRepository;
    }

    @Override
    public FavoriteResponse addFavorite(FavoriteCreateRequest favoriteCreateRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(authentication.getName());

        Optional<Favorite> optionalFavorite =
                this.favoriteRepository.findByUserIdAndMediaIdAndMediaType(
                        userId,
                        favoriteCreateRequest.getMediaId(),
                        favoriteCreateRequest.getMediaType()
                );

        if (optionalFavorite.isPresent()) {
            return this.favoriteMapper.toFavoriteResponse(optionalFavorite.get());
        }

        User user = this.userRepository.getReferenceById(userId);

        Favorite favorite = this.favoriteMapper.toFavorite(favoriteCreateRequest);
        favorite.setUser(user);

        favorite = this.favoriteRepository.save(favorite);

        return this.favoriteMapper.toFavoriteResponse(favorite);
    }

    @Override
    public void removeFavorite(Long favoriteId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(authentication.getName());

        Favorite favorite = this.favoriteRepository
                .findByIdAndUserId(favoriteId, userId)
                .orElseThrow(() -> new AppException(ErrorCode.FAVORITE_NOT_FOUND));

        this.favoriteRepository.delete(favorite);
    }

    @Override
    public List<FavoriteResponse> getFavoritesOfUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(authentication.getName());

        return this.favoriteRepository
                .findAllByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(this.favoriteMapper::toFavoriteResponse)
                .toList();
    }
}
