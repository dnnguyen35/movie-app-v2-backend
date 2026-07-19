package com.example.abcxyz.service.implement;

import com.example.abcxyz.dto.response.MovieStatsResponse;
import com.example.abcxyz.dto.response.ReviewResponse;
import com.example.abcxyz.dto.response.UserStatsResponse;
import com.example.abcxyz.entity.Review;
import com.example.abcxyz.entity.User;
import com.example.abcxyz.enums.ErrorCode;
import com.example.abcxyz.enums.RoleType;
import com.example.abcxyz.exception.AppException;
import com.example.abcxyz.mapper.ReviewMapper;
import com.example.abcxyz.repository.FavoriteRepository;
import com.example.abcxyz.repository.ReviewRepository;
import com.example.abcxyz.repository.UserRepository;
import com.example.abcxyz.service.AdminService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;
    private final FavoriteRepository favoriteRepository;
    private final ReviewMapper reviewMapper;

    public AdminServiceImpl(UserRepository userRepository, ReviewRepository reviewRepository,
                            FavoriteRepository favoriteRepository, ReviewMapper reviewMapper) {
        this.userRepository = userRepository;
        this.reviewRepository = reviewRepository;
        this.favoriteRepository = favoriteRepository;
        this.reviewMapper = reviewMapper;
    }

    @Override
    public List<UserStatsResponse> getAllUsersWithStats() {
        return this.userRepository.getAllUsersWithStats(RoleType.ROLE_ADMIN);
    }

    @Override
    public List<ReviewResponse> getAllReviews() {
        return this.reviewRepository.getAllWithUSerInfo().stream().map(this.reviewMapper::toReviewResponse).toList();
    }

    @Override
    public List<MovieStatsResponse> getAllMoviesWithStats() {
        return this.favoriteRepository.getAllMoviesWithStats();
    }

    @Override
    @Transactional
    public void lockUser(Long userId) {
        User lockedUser = this.userRepository.findById(userId).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_FOUND)
        );

        if (!lockedUser.isActive()) {
            return;
        }

        lockedUser.setActive(false);
        this.userRepository.save(lockedUser);
    }

    @Override
    public void unLockUser(Long userId) {
        User unLockedUser = this.userRepository.findById(userId).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_FOUND)
        );

        if (unLockedUser.isActive()) {
            return;
        }

        unLockedUser.setActive(true);
        this.userRepository.save(unLockedUser);
    }

    @Override
    public void removeReview(Long reviewId) {
        Review deletedReview = this.reviewRepository.findById(reviewId).orElseThrow(
                () -> new AppException(ErrorCode.REVIEW_NOT_FOUND)
        );

        this.reviewRepository.deleteById(reviewId);
    }
}
