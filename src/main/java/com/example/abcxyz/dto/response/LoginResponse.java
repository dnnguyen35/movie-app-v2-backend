package com.example.abcxyz.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
@AllArgsConstructor
@Setter
public class LoginResponse {

    private String accessToken;
    private String refreshToken;
    private Long id;
    private UserResponse user;
}
