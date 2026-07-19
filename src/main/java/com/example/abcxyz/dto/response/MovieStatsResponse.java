package com.example.abcxyz.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
@AllArgsConstructor
public class MovieStatsResponse {

    private String mediaId;
    private String mediaTitle;
    private BigDecimal mediaRate;
    private Long totalReviews;
    private Long totalFavorites;
}
