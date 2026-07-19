package com.example.abcxyz.service;

import com.example.abcxyz.dto.request.ReviewCreateRequest;
import com.example.abcxyz.dto.response.ReviewResponse;

import java.util.List;

public interface ReviewService {
    ReviewResponse createReview(ReviewCreateRequest reviewCreateRequest);

    void removeReview(Long reviewId);

    List<ReviewResponse> getReviewsOfUser();
}
