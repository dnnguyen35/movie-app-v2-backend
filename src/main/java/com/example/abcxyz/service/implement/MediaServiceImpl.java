package com.example.abcxyz.service.implement;

import com.example.abcxyz.configuration.TmdbApi;
import com.example.abcxyz.entity.Review;
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

    private final TmdbApi tmdbApi;
    private final FavoriteRepository favoriteRepository;
    private final ReviewRepository reviewRepository;
    private final TaskExecutor completableTaskExecutor;

    public MediaServiceImpl(TmdbApi tmdbApi, FavoriteRepository favoriteRepository, ReviewRepository reviewRepository
            , @Qualifier("completableTaskExecutor") TaskExecutor completableTaskExecutor) {
        this.tmdbApi = tmdbApi;
        this.favoriteRepository = favoriteRepository;
        this.reviewRepository = reviewRepository;
        this.completableTaskExecutor = completableTaskExecutor;
    }

    @Override
    public Object getList(String mediaType, String mediaCategory, Integer page, String language) {
        return this.tmdbApi.getMediaList(mediaType, mediaCategory, page, language);
    }

    @Override
    public Object getGenres(String mediaType, String language) {
        return this.tmdbApi.getMediaGenres(mediaType, language);
    }

    @Override
    public Object getSearch(String mediaType, String query, Integer page, String language) {
        String finalMediaType = mediaType.equals("people") ? "person" : mediaType;

        return this.tmdbApi.getMediaSearch(finalMediaType, query, page, language);
    }

    @Override
    public Object getDetail(String mediaType, Long mediaId, String language) {
        Map<String, Object> media = new HashMap<>();

        CompletableFuture<Object> detail = CompletableFuture.supplyAsync(() ->
                        (Object) this.tmdbApi.getMediaDetail(mediaType, mediaId, language),
                completableTaskExecutor
        );

        CompletableFuture<Object> credits = CompletableFuture.supplyAsync(() ->
                        (Object) this.tmdbApi.getMediaCredits(mediaType, mediaId, language),
                completableTaskExecutor
        );

        CompletableFuture<Object> videos = CompletableFuture.supplyAsync(() ->
                        (Object) this.tmdbApi.getMediaVideos(mediaType, mediaId),
                completableTaskExecutor
        );

        CompletableFuture<Object> recommend = CompletableFuture.supplyAsync(() ->
                        (Object) this.tmdbApi.getMediaRecommend(mediaType, mediaId, language),
                completableTaskExecutor
        );

        CompletableFuture<Object> images = CompletableFuture.supplyAsync(() ->
                        (Object) this.tmdbApi.getMediaImages(mediaType, mediaId, language),
                completableTaskExecutor
        );

        CompletableFuture<List<Review>> reviews = CompletableFuture.supplyAsync(() ->
                        (List<Review>) this.reviewRepository.findByMediaIdWithUserInfo(String.valueOf(mediaId)),
                completableTaskExecutor
        );

        CompletableFuture<Boolean> isFavorite;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getName())) {
            Long userId = Long.parseLong(authentication.getName());

            isFavorite = CompletableFuture.supplyAsync(() ->
                            (Boolean) this.favoriteRepository.existsByMediaIdAndUser_Id(String.valueOf(mediaId), userId),
                    completableTaskExecutor);
        } else {
            isFavorite = CompletableFuture.completedFuture(false);
        }

        CompletableFuture.allOf(
                detail,
                credits,
                videos,
                recommend,
                images,
                reviews,
                isFavorite
        ).join();

        media.put("detail", detail.join());
        media.put("credits", credits.join());
        media.put("videos", videos.join());
        media.put("recommend", recommend.join());
        media.put("images", images.join());
        media.put("reviews", reviews.join());
        media.put("isFavorite", isFavorite.join());

        return media;
    }
}
