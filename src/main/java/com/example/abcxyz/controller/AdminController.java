package com.example.abcxyz.controller;

import com.example.abcxyz.dto.ApiResponse;
import com.example.abcxyz.dto.response.MovieStatsResponse;
import com.example.abcxyz.dto.response.ReviewResponse;
import com.example.abcxyz.dto.response.UserStatsResponse;
import com.example.abcxyz.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/users-stats")
    public ResponseEntity<ApiResponse<List<UserStatsResponse>>> getAllUsersWithStats() {
        return ResponseEntity.ok(ApiResponse.success(200, "Successfully", this.adminService.getAllUsersWithStats()));
    }

    @GetMapping("/reviews-stats")
    public ResponseEntity<ApiResponse<List<ReviewResponse>>> getAllReviews() {
        return ResponseEntity.ok(ApiResponse.success(200, "Successfully", this.adminService.getAllReviews()));
    }

    @GetMapping("/movies-stats")
    public ResponseEntity<ApiResponse<List<MovieStatsResponse>>> getAllMoviesWithStats() {
        return ResponseEntity.ok(ApiResponse.success(200, "Successfully", this.adminService.getAllMoviesWithStats()));
    }

    @DeleteMapping("/reviews/{reviewId}")
    public ResponseEntity<ApiResponse<?>> removeReview(@PathVariable("reviewId") Long reviewId) {
        this.adminService.removeReview(reviewId);

        return ResponseEntity.ok(ApiResponse.success(200, "Successfully", null));
    }

    @PutMapping("/lock/{userId}")
    public ResponseEntity<ApiResponse<?>> lockUser(@PathVariable("userId") Long userId) {
        this.adminService.lockUser(userId);

        return ResponseEntity.ok(ApiResponse.success(200, "Successfully", null));
    }

    @PutMapping("/unlock/{userId}")
    public ResponseEntity<ApiResponse<?>> unlockUser(@PathVariable("userId") Long userId) {
        this.adminService.unLockUser(userId);

        return ResponseEntity.ok(ApiResponse.success(200, "Successfully", null));
    }
}
