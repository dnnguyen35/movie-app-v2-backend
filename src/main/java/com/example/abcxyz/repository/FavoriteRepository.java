package com.example.abcxyz.repository;

import com.example.abcxyz.dto.response.MovieStatsResponse;
import com.example.abcxyz.entity.Favorite;
import com.example.abcxyz.enums.MediaType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    boolean existsByMediaIdAndUser_Id(String mediaId, Long userId);

    @Query("""
            select new com.example.abcxyz.dto.response.MovieStatsResponse(
                f.mediaId,
                max(f.mediaTitle),
                max(f.mediaRate),
                count(distinct r.id),
                count(distinct f.id)
            )
            from Favorite f
            left join Review r on r.mediaId = f.mediaId
            group by f.mediaId
            order by
                count(distinct r.id) desc,
                count(distinct f.id) desc
            """)
    List<MovieStatsResponse> getAllMoviesWithStats();

    Optional<Favorite> findByUserIdAndMediaIdAndMediaType(
            Long userId,
            String mediaId,
            MediaType mediaType
    );

    Optional<Favorite> findByIdAndUserId(
            Long favoriteId,
            Long userId
    );

    List<Favorite> findAllByUserIdOrderByCreatedAtDesc(Long userId);

}
