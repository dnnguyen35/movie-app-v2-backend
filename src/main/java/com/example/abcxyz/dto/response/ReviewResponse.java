package com.example.abcxyz.dto.response;

import com.example.abcxyz.enums.MediaType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Getter
@Builder
@AllArgsConstructor
public class ReviewResponse {

    private Long id;
    private String content;
    private MediaType mediaType;
    private String mediaId;
    private String mediaTitle;
    private String mediaPoster;
    private Instant createdAt;
    private UserResponse user;
}
