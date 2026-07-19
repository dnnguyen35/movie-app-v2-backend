package com.example.abcxyz.mapper;

import com.example.abcxyz.dto.request.ReviewCreateRequest;
import com.example.abcxyz.dto.response.ReviewResponse;
import com.example.abcxyz.entity.Review;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",
        uses = {UserMapper.class}
)
public interface ReviewMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Review toReview(ReviewCreateRequest reviewCreateRequest);

    ReviewResponse toReviewResponse(Review review);
}
