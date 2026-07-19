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

    @GetMapping("/{mediaType}/list/{mediaCategory}")
    public ResponseEntity<ApiResponse<Object>> getList(@PathVariable("mediaType") String mediaType,
                                                       @PathVariable("mediaCategory") String mediaCategory,
                                                       @RequestParam(value = "page", defaultValue = "1") int page,
                                                       @RequestHeader(value = "Accept-Language", defaultValue = "en") String language
    ) {
        return ResponseEntity.ok(ApiResponse.success(200, "Success", this.mediaService.getList(mediaType,
                mediaCategory, page, language)));
    }

    @GetMapping("/{mediaType}/genres")
    public ResponseEntity<ApiResponse<Object>> getGenres(@PathVariable("mediaType") String mediaType,
                                                         @RequestHeader(value = "Accept-Language", defaultValue = "en") String language
    ) {
        return ResponseEntity.ok(ApiResponse.success(200, "Success", this.mediaService.getGenres(mediaType, language)));
    }

    @GetMapping("/{mediaType}/search")
    public ResponseEntity<ApiResponse<Object>> getSearch(@PathVariable("mediaType") String mediaType,
                                                         @RequestParam("query") String query,
                                                         @RequestParam(value = "page", defaultValue = "1") int page,
                                                         @RequestHeader(value = "Accept-Language", defaultValue = "en") String language
    ) {
        return ResponseEntity.ok(ApiResponse.success(200, "Success", this.mediaService.getSearch(mediaType, query,
                page, language)));
    }

    @GetMapping("/{mediaType}/detail/{mediaId}")
    public ResponseEntity<ApiResponse<Object>> getDetail(@PathVariable("mediaType") String mediaType,
                                                         @PathVariable("mediaId") Long mediaId,
                                                         @RequestHeader(value = "Accept-Language",
                                                                 defaultValue = "en") String language
    ) {
        return ResponseEntity.ok(ApiResponse.success(200, "Success", this.mediaService.getDetail(mediaType, mediaId,
                language)));
    }
}
