package com.example.abcxyz.controller;

import com.example.abcxyz.dto.ApiResponse;
import com.example.abcxyz.dto.request.FavoriteCreateRequest;
import com.example.abcxyz.dto.response.FavoriteResponse;
import com.example.abcxyz.service.FavoriteService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/favorites")
@PreAuthorize("isAuthenticated()")
public class FavoriteController {

    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<FavoriteResponse>> addFavorite(
            @RequestBody @Valid FavoriteCreateRequest favoriteCreateRequest
    ) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        201,
                        "Successfully",
                        this.favoriteService.addFavorite(favoriteCreateRequest)
                )
        );
    }

    @DeleteMapping("/{favoriteId}")
    public ResponseEntity<ApiResponse<?>> removeFavorite(
            @PathVariable("favoriteId") Long favoriteId
    ) {
        this.favoriteService.removeFavorite(favoriteId);

        return ResponseEntity.ok(
                ApiResponse.success(
                        200,
                        "Successfully",
                        null
                )
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<FavoriteResponse>>> getFavoritesOfUser() {
        return ResponseEntity.ok(
                ApiResponse.success(
                        200,
                        "Successfully",
                        this.favoriteService.getFavoritesOfUser()
                )
        );
    }
}
