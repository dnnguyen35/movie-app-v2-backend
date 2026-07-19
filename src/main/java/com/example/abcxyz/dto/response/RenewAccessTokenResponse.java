package com.example.abcxyz.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class RenewAccessTokenResponse {

    private String newAccessToken;
}
