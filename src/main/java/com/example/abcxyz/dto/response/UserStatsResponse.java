package com.example.abcxyz.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Getter
@Builder
@AllArgsConstructor
public class UserStatsResponse {

    private Long id;
    private String email;
    private String name;
    private Boolean active;
    private Instant createdAt;
    private Long totalReviews;
    private Long totalFavorites;
}
