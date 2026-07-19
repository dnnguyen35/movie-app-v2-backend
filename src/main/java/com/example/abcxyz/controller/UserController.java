package com.example.abcxyz.controller;

import com.example.abcxyz.dto.ApiResponse;
import com.example.abcxyz.dto.request.ChangePasswordRequest;
import com.example.abcxyz.dto.response.ChangePasswordResponse;
import com.example.abcxyz.dto.response.UserResponse;
import com.example.abcxyz.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@PreAuthorize("isAuthenticated()")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping("/change-password")
    public ResponseEntity<ApiResponse<ChangePasswordResponse>> changePassword(@RequestBody @Valid ChangePasswordRequest changePasswordRequest) {
        return ResponseEntity.ok(ApiResponse.success(200, "Successfully",
                this.userService.changePassword(changePasswordRequest)));
    }

    @GetMapping("/info")
    public ResponseEntity<ApiResponse<UserResponse>> getInfo() {
        return ResponseEntity.ok(ApiResponse.success(200, "Successfully", this.userService.getInfo()));
    }
}
