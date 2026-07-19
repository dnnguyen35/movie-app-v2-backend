package com.example.abcxyz.service;

import com.example.abcxyz.dto.response.MovieStatsResponse;
import com.example.abcxyz.dto.response.ReviewResponse;
import com.example.abcxyz.dto.response.UserStatsResponse;

import java.util.List;

public interface AdminService {

    List<UserStatsResponse> getAllUsersWithStats();

    List<ReviewResponse> getAllReviews();

    List<MovieStatsResponse> getAllMoviesWithStats();

    void lockUser(Long userId);

    void unLockUser(Long userId);

    void removeReview(Long reviewId);
}
