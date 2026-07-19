package com.example.abcxyz.repository;

import com.example.abcxyz.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("""
            select r
            from Review r
            join fetch r.user u
            left join fetch u.roles
            where r.mediaId = :mediaId
            """)
    List<Review> findByMediaIdWithUserInfo(@Param("mediaId") String mediaId);

    @Query("""
            select r
            from Review r
            join fetch r.user u
            left join fetch u.roles
            order by r.createdAt desc
            """)
    List<Review> getAllWithUSerInfo();

    @Query("""
                        select r
                        from Review r
                        join fetch r.user u
                        left join fetch u.roles
                        where u.id = :userId
                        order by r.createdAt desc
            """)
    List<Review> getAllOfUserWithUserInfo(@Param("userId") Long userId);
}
