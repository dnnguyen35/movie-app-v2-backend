package com.example.abcxyz.dto.response;

import com.example.abcxyz.enums.MediaType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Builder
@AllArgsConstructor
public class FavoriteResponse {

    private Long id;

    private MediaType mediaType;

    private String mediaId;

    private String mediaTitle;

    private String mediaPoster;

    private BigDecimal mediaRate;

    private Instant createdAt;
}
