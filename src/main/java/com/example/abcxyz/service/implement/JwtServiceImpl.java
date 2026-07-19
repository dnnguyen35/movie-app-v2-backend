package com.example.abcxyz.service.implement;

import com.example.abcxyz.entity.User;
import com.example.abcxyz.enums.ErrorCode;
import com.example.abcxyz.exception.AppException;
import com.example.abcxyz.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.sql.Date;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class JwtServiceImpl implements JwtService {

    @Value("${jwt.access-token-key}")
    private String accessTokenKey;

    @Value("${jwt.refresh-token-key}")
    private String refreshTokenKey;

    @Value("${jwt.access-token-live-time}")
    private long accessTokenLiveTime;

    @Value("${jwt.refresh-token-live-time}")
    private long refreshTokenLiveTime;

    public String generateAccessToken(User user) {
        Instant now = Instant.now();
        Instant expireTime = now.plus(this.accessTokenLiveTime, ChronoUnit.MINUTES);

        List<String> roles = user.getRoles().stream().map(
                        role -> role.getRoleType().name()
                )
                .toList();

        Map<String, Object> claims = new HashMap<>();
        claims.put("email", user.getEmail());
        claims.put("roles", roles);
        claims.put("type", "access");

        return Jwts.builder()
                .subject(user.getId().toString())
                .claims(claims)
                .issuedAt(Date.from(now))
                .expiration(Date.from(expireTime))
                .signWith(this.getAccessKey())
                .compact();
    }

    public String generateRefreshToken(User user) {
        Instant now = Instant.now();
        Instant expireTime = now.plus(this.refreshTokenLiveTime, ChronoUnit.DAYS);

        return Jwts.builder()
                .subject(user.getId().toString())
                .issuedAt(Date.from(now))
                .expiration(Date.from(expireTime))
                .signWith(this.getRefreshKey())
                .compact();
    }

    @Override
    public Claims extractAccessTokenClaims(String accessToken) {
        try {
            return Jwts.parser()
                    .verifyWith(this.getAccessKey())
                    .build()
                    .parseSignedClaims(accessToken)
                    .getPayload();
        } catch (ExpiredJwtException ex) {
            throw new AppException(ErrorCode.EXPIRED_ACCESS_TOKEN);
        } catch (JwtException ex) {
            throw new AppException(ErrorCode.INVALID_ACCESS_TOKEN);
        }
    }

    @Override
    public Claims extractRefreshTokenClaims(String refreshToken) {
        try {
            return Jwts.parser()
                    .verifyWith(this.getRefreshKey())
                    .build()
                    .parseSignedClaims(refreshToken)
                    .getPayload();
        } catch (ExpiredJwtException ex) {
            throw new AppException(ErrorCode.EXPIRED_REFRESH_TOKEN);
        } catch (JwtException ex) {
            throw new AppException(ErrorCode.INVALID_REFRESH_TOKEN);
        }
    }

    private SecretKey getAccessKey() {
        byte[] accessKeyBytes = Decoders.BASE64.decode(this.accessTokenKey);

        return Keys.hmacShaKeyFor(accessKeyBytes);
    }

    private SecretKey getRefreshKey() {
        byte[] refreshKeyBytes = Decoders.BASE64.decode(this.refreshTokenKey);

        return Keys.hmacShaKeyFor(refreshKeyBytes);
    }
}
