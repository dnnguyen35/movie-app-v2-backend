package com.example.abcxyz.mapper;

import com.example.abcxyz.dto.request.RegisterRequest;
import com.example.abcxyz.dto.response.UserResponse;
import com.example.abcxyz.entity.Role;
import com.example.abcxyz.entity.User;
import com.example.abcxyz.enums.RoleType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    User toUser(RegisterRequest registerRequest);

    @Mapping(target = "roles", source = "roles")
    UserResponse toUserResponse(User user);

    default Set<String> mapSetRoleToSetString(Set<Role> roles) {
        if (roles == null) {
            return Set.of();
        }
        return roles.stream().map(Role::getRoleType).map(RoleType::name).collect(Collectors.toSet());
    }

}
