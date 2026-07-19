package com.example.abcxyz.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum MediaType {
    MOVIE,
    TV;

    @JsonCreator
    public static MediaType from(String value) {
        return MediaType.valueOf(value.toUpperCase());
    }
}
