package com.example.abcxyz.repository;

import com.example.abcxyz.dto.response.UserStatsResponse;
import com.example.abcxyz.entity.User;
import com.example.abcxyz.enums.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    @Query("""
            select new com.example.abcxyz.dto.response.UserStatsResponse(
                u.id,
                u.email,
                u.name,
                u.active,
                u.createdAt,
                count(distinct r.id),
                count(distinct f.id)
            )
            from User u
            left join Review r on r.user = u
            left join Favorite f on f.user = u
            where not exists (
                select 1
                from u.roles role
                where role.roleType = :roleType
            )
            group by
                u.id,
                u.email,
                u.name,
                u.active,
                u.createdAt
            """)
    List<UserStatsResponse> getAllUsersWithStats(@Param("roleType") RoleType roleType);
}