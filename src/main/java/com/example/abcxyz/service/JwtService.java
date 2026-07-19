package com.example.abcxyz.service;

import com.example.abcxyz.entity.User;
import io.jsonwebtoken.Claims;

public interface JwtService {

    String generateAccessToken(User user);

    String generateRefreshToken(User user);

    Claims extractAccessTokenClaims(String accessToken);

    Claims extractRefreshTokenClaims(String refreshToken);
}
