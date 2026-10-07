package com.example.abcxyz.service.implement;

import com.example.abcxyz.configuration.KkmovieApi;
import com.example.abcxyz.entity.Review;
import com.example.abcxyz.mapper.ReviewMapper;
import com.example.abcxyz.repository.FavoriteRepository;
import com.example.abcxyz.repository.ReviewRepository;
import com.example.abcxyz.service.MediaService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
public class MediaServiceImpl implements MediaService {

    private final KkmovieApi kkmovieApi;
    private final FavoriteRepository favoriteRepository;
    private final ReviewRepository reviewRepository;
    private final TaskExecutor completableTaskExecutor;
    private final ReviewMapper reviewMapper;

    public MediaServiceImpl(KkmovieApi kkmovieApi, FavoriteRepository favoriteRepository, ReviewRepository reviewRepository
            , @Qualifier("completableTaskExecutor") TaskExecutor completableTaskExecutor, ReviewMapper reviewMapper) {
        this.kkmovieApi = kkmovieApi;
        this.favoriteRepository = favoriteRepository;
        this.reviewRepository = reviewRepository;
        this.completableTaskExecutor = completableTaskExecutor;
        this.reviewMapper = reviewMapper;
    }

    @Override
    public Object getList(Integer page) {
        return this.kkmovieApi.getMediaList(page);
    }

    @Override
    public Object getListByType(String type, Integer page) {
        return this.kkmovieApi.getMediaListByType(type, page);
    }

    @Override
    public Object getImages(String mediaSlug) {
        return this.kkmovieApi.getMediaImages(mediaSlug);
    }

    @Override
    public Object getGenres() {
        return this.kkmovieApi.getGenres();
    }

    @Override
    public Object getListByGenre(String genreSlug, Integer page) {
        return this.kkmovieApi.getMediaListByGenre(genreSlug, page);
    }

    @Override
    public Object getCountries() {
        return this.kkmovieApi.getCountries();
    }

    @Override
    public Object getListByCountry(String countrySlug, Integer page) {
        return this.kkmovieApi.getMediaListByCountry(countrySlug, page);
    }

    @Override
    public Object getSearch(String keyword, Integer page) {
        return this.kkmovieApi.getSearch(keyword, page);
    }

    @Override
    public Object getDetail(String mediaSlug, String mediaGenre, String mediaId) {
        Map<String, Object> media = new HashMap<>();

        CompletableFuture<Object> detail = CompletableFuture.supplyAsync(
                () -> (Object) this.kkmovieApi.getMediaDetail(mediaSlug),
                completableTaskExecutor
        );

        CompletableFuture<Object> recommend = CompletableFuture.supplyAsync(
                () -> (Object) this.kkmovieApi.getMediaListByGenre(mediaGenre, 1),
                completableTaskExecutor
        );

        CompletableFuture<Object> images = CompletableFuture.supplyAsync(
                () -> (Object) this.kkmovieApi.getMediaImages(mediaSlug),
                completableTaskExecutor
        );

        CompletableFuture<List<Review>> reviews = CompletableFuture.supplyAsync(
                () -> (List<Review>) this.reviewRepository.findByMediaIdWithUserInfo(mediaId),
                completableTaskExecutor
        );

        CompletableFuture<Boolean> isFavorite;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getName())) {
            Long userId = Long.parseLong(authentication.getName());

            isFavorite = CompletableFuture.supplyAsync(
                    () -> (Boolean) this.favoriteRepository.existsByMediaIdAndUser_Id(mediaId, userId),
                    completableTaskExecutor
            );
        } else {
            isFavorite = CompletableFuture.completedFuture(false);
        }

        CompletableFuture.allOf(
                detail,
                recommend,
                images,
                reviews,
                isFavorite
        ).join();

        media.put("detail", ((Map<String, Object>) detail.join()).get("data"));
        media.put("recommend", ((Map<String, Object>) recommend.join()).get("data"));
        media.put("images", ((Map<String, Object>) images.join()).get("data"));
        media.put("reviews", reviews.join());
        media.put("isFavorite", isFavorite.join());

        return media;
    }
}
