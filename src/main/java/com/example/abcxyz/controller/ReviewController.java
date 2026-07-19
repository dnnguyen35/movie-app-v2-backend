package com.example.abcxyz.controller;

import com.example.abcxyz.dto.ApiResponse;
import com.example.abcxyz.dto.request.ReviewCreateRequest;
import com.example.abcxyz.dto.response.ReviewResponse;
import com.example.abcxyz.service.ReviewService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reviews")
@PreAuthorize("isAuthenticated()")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ReviewResponse>> createReview(@RequestBody @Valid ReviewCreateRequest reviewCreateRequest) {
        return ResponseEntity.ok(ApiResponse.success(201, "Successfully",
                this.reviewService.createReview(reviewCreateRequest)));
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<ApiResponse<?>> removeReview(@PathVariable("reviewId") Long reviewId) {
        this.reviewService.removeReview(reviewId);

        return ResponseEntity.ok(ApiResponse.success(200, "Successfully", null));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ReviewResponse>>> getReviewsOfUser() {
        return ResponseEntity.ok(ApiResponse.success(200, "Successfully", this.reviewService.getReviewsOfUser()));
    }
}
