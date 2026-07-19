package com.example.abcxyz.dto.request;

import com.example.abcxyz.enums.MediaType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class ReviewCreateRequest {

    @NotBlank(message = "Content is required")
    private String content;

    @NotNull(message = "MediaType is required")
    private MediaType mediaType;

    @NotBlank(message = "MediaTitle is required")
    private String mediaTitle;

    @NotBlank(message = "MediaId is required")
    private String mediaId;

    @NotBlank(message = "MediaPoster is required")
    private String mediaPoster;
}
