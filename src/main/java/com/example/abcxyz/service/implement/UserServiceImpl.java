package com.example.abcxyz.service.implement;

import com.example.abcxyz.dto.request.ChangePasswordRequest;
import com.example.abcxyz.dto.response.ChangePasswordResponse;
import com.example.abcxyz.dto.response.UserResponse;
import com.example.abcxyz.entity.User;
import com.example.abcxyz.enums.ErrorCode;
import com.example.abcxyz.exception.AppException;
import com.example.abcxyz.mapper.UserMapper;
import com.example.abcxyz.repository.UserRepository;
import com.example.abcxyz.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    @Override
    public ChangePasswordResponse changePassword(ChangePasswordRequest changePasswordRequest) {
        String currentPassword = changePasswordRequest.getCurrentPassword();
        String newPassword = changePasswordRequest.getNewPassword();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Long userId = Long.parseLong(authentication.getName());

        User user = this.userRepository.findById(userId).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_FOUND)
        );

        if (!user.isActive()) {
            throw new AppException(ErrorCode.USER_LOCKED);
        }

        if (!this.passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new AppException(ErrorCode.INVALID_PASSWORD);
        }

        String newPasswordHash = this.passwordEncoder.encode(newPassword);

        user.setPassword(newPasswordHash);

        this.userRepository.save(user);

        return ChangePasswordResponse.builder()
                .message("Change password successfully")
                .build();
    }

    @Override
    public UserResponse getInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Long userId = Long.parseLong(authentication.getName());

        User user = this.userRepository.findById(userId).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_FOUND)
        );

        return this.userMapper.toUserResponse(user);
    }
}
