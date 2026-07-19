package com.example.abcxyz.repository;

import com.example.abcxyz.entity.Role;
import com.example.abcxyz.enums.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByRoleType(RoleType roleType);
}
