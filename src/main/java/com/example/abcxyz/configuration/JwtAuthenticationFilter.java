package com.example.abcxyz.configuration;

import com.example.abcxyz.exception.AppException;
import com.example.abcxyz.service.JwtService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final AuthenticationEntryPoint authenticationEntryPoint;

    public JwtAuthenticationFilter(
            JwtService jwtService,
            AuthenticationEntryPoint authenticationEntryPoint
    ) {
        this.jwtService = jwtService;
        this.authenticationEntryPoint = authenticationEntryPoint;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        MDC.put("traceId", UUID.randomUUID().toString().substring(0, 7));

        String xForwardedFor = request.getHeader("X-Forwarded-For");
        String ipAddress = (xForwardedFor != null && !xForwardedFor.isBlank())
                ? xForwardedFor.split(",")[0].trim()
                : request.getRemoteAddr();

        MDC.put("ipAddress", ipAddress);

        try {
            String accessToken = getAccessTokenFromRequestHeader(request);

            if (StringUtils.hasText(accessToken)) {

                Claims claims = jwtService.extractAccessTokenClaims(accessToken);

                String userId = claims.getSubject();
                MDC.put("userId", userId);

                List<?> roles = claims.get("roles", List.class);

                List<SimpleGrantedAuthority> authorities =
                        roles == null
                                ? List.of()
                                : roles.stream()
                                .map(role -> new SimpleGrantedAuthority(role.toString()))
                                .toList();

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userId,
                                null,
                                authorities
                        );

                authentication.setDetails(
                        new WebAuthenticationDetailsSource()
                                .buildDetails(request)
                );

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

            filterChain.doFilter(request, response);

        } catch (AppException ex) {

            SecurityContextHolder.clearContext();

            request.setAttribute("accessTokenError", ex);

            authenticationEntryPoint.commence(
                    request,
                    response,
                    new InsufficientAuthenticationException(
                            ex.getMessage(),
                            ex
                    )
            );

            return;

        } finally {
            MDC.clear();
        }
    }

    private String getAccessTokenFromRequestHeader(HttpServletRequest request) {

        String bearerToken = request.getHeader("Authorization");

        if (StringUtils.hasText(bearerToken)
                && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }

        return null;
    }
}