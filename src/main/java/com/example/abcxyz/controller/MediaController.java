package com.example.abcxyz.controller;

import com.example.abcxyz.dto.ApiResponse;
import com.example.abcxyz.service.MediaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/medias")
public class MediaController {

    private final MediaService mediaService;

    public MediaController(MediaService mediaService) {
        this.mediaService = mediaService;
    }

    @GetMapping("/list")
    public ResponseEntity<ApiResponse<Object>> getList(@RequestParam(value = "page", defaultValue = "1") Integer page) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        200,
                        "Success",
                        this.mediaService.getList(page)
                )
        );
    }

    @GetMapping("/list/{type}")
    public ResponseEntity<ApiResponse<Object>> getListByType(@PathVariable("type") String type,
                                                             @RequestParam(value = "page", defaultValue = "1") Integer page) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        200,
                        "Success",
                        this.mediaService.getListByType(type, page)
                )
        );
    }

    @GetMapping("/detail/{mediaSlug}")
    public ResponseEntity<ApiResponse<Object>> getDetail(@PathVariable("mediaSlug") String mediaSlug,
                                                         @RequestParam("mediaGenre") String mediaGenre,
                                                         @RequestParam("mediaId") String mediaId) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        200,
                        "Success",
                        this.mediaService.getDetail(mediaSlug, mediaGenre, mediaId)
                )
        );
    }

    @GetMapping("/images/{mediaSlug}")
    public ResponseEntity<ApiResponse<Object>> getImages(@PathVariable("mediaSlug") String mediaSlug) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        200,
                        "Success",
                        this.mediaService.getImages(mediaSlug)
                )
        );
    }

    @GetMapping("/genres")
    public ResponseEntity<ApiResponse<Object>> getGenres() {
        return ResponseEntity.ok(
                ApiResponse.success(
                        200,
                        "Success",
                        this.mediaService.getGenres()
                )
        );
    }

    @GetMapping("/genres/{genreSlug}")
    public ResponseEntity<ApiResponse<Object>> getListByGenre(@PathVariable("genreSlug") String genreSlug,
                                                              @RequestParam(value = "page", defaultValue = "1") Integer page) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        200,
                        "Success",
                        this.mediaService.getListByGenre(genreSlug, page)
                )
        );
    }

    @GetMapping("/countries")
    public ResponseEntity<ApiResponse<Object>> getCountries() {
        return ResponseEntity.ok(
                ApiResponse.success(
                        200,
                        "Success",
                        this.mediaService.getCountries()
                )
        );
    }

    @GetMapping("/country/{countrySlug}")
    public ResponseEntity<ApiResponse<Object>> getListByCountry(@PathVariable("countrySlug") String countrySlug,
                                                                @RequestParam(value = "page", defaultValue = "1") Integer page) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        200,
                        "Success",
                        this.mediaService.getListByCountry(countrySlug, page)
                )
        );
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Object>> getSearch(@RequestParam(value = "keyword", defaultValue = "") String keyword,
                                                         @RequestParam(value = "page", defaultValue = "1") Integer page) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        200,
                        "Success",
                        this.mediaService.getSearch(keyword, page)
                )
        );
    }
}
