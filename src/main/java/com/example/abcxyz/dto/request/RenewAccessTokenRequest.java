package com.example.abcxyz.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class RenewAccessTokenRequest {

    @NotBlank(message = "RefreshToken is required")
    private String refreshToken;
}
