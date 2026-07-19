package com.example.abcxyz.dto.request;

import com.example.abcxyz.enums.MediaType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@NoArgsConstructor
@Setter
@Getter
public class FavoriteCreateRequest {

    @NotNull(message = "MediaType is required")
    private MediaType mediaType;

    @NotBlank(message = "MediaId is required")
    private String mediaId;

    @NotBlank(message = "MediaTitle is required")
    private String mediaTitle;

    @NotBlank(message = "MediaPoster is required")
    private String mediaPoster;

    @NotNull(message = "MediaRate is required")
    private BigDecimal mediaRate;
}
