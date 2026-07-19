package com.example.abcxyz.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class VerifyOtpResponse {

    private String accessToken;
    private String refreshToken;
    private Long id;
    private UserResponse user;
}
