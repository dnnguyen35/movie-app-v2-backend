package com.example.abcxyz.service;

import com.example.abcxyz.dto.request.ChangePasswordRequest;
import com.example.abcxyz.dto.response.ChangePasswordResponse;
import com.example.abcxyz.dto.response.UserResponse;

public interface UserService {

    ChangePasswordResponse changePassword(ChangePasswordRequest changePasswordRequest);

    UserResponse getInfo();
}
