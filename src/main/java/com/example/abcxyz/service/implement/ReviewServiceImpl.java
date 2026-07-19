package com.example.abcxyz.service.implement;

import com.example.abcxyz.dto.request.ReviewCreateRequest;
import com.example.abcxyz.dto.response.ReviewResponse;
import com.example.abcxyz.entity.Review;
import com.example.abcxyz.entity.User;
import com.example.abcxyz.enums.ErrorCode;
import com.example.abcxyz.exception.AppException;
import com.example.abcxyz.mapper.ReviewMapper;
import com.example.abcxyz.repository.ReviewRepository;
import com.example.abcxyz.repository.UserRepository;
import com.example.abcxyz.service.ReviewService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final ReviewMapper reviewMapper;

    public ReviewServiceImpl(ReviewRepository reviewRepository, UserRepository userRepository, ReviewMapper reviewMapper) {
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.reviewMapper = reviewMapper;
    }

    @Override
    public ReviewResponse createReview(ReviewCreateRequest reviewCreateRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Long userId = Long.parseLong(authentication.getName());

        User user = this.userRepository.findById(userId).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_FOUND)
        );

        Review review = this.reviewMapper.toReview(reviewCreateRequest);

        review.setUser(user);

        Review savedReview = this.reviewRepository.save(review);


        return this.reviewMapper.toReviewResponse(savedReview);
    }

    @Override
    @Transactional
    public void removeReview(Long reviewId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Long userId = Long.parseLong(authentication.getName());

        Review deletedReview = this.reviewRepository.findById(reviewId).orElseThrow(
                () -> new AppException(ErrorCode.REVIEW_NOT_FOUND)
        );

        if (!deletedReview.getUser().getId().equals(userId)) {
            throw new AppException(ErrorCode.FORBIDDEN);
        }

        this.reviewRepository.deleteById(reviewId);
    }

    @Override
    public List<ReviewResponse> getReviewsOfUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Long userId = Long.parseLong(authentication.getName());

        return this.reviewRepository.getAllOfUserWithUserInfo(userId)
                .stream()
                .map(this.reviewMapper::toReviewResponse)
                .toList();
    }
}
