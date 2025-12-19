package com.buy01.user.controller;

import com.buy01.user.dto.RegisterRequest;
import com.buy01.user.model.User;
import com.buy01.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody RegisterRequest request) {
        // Map DTO to Entity (Manual mapping for pedagogy, usually MapStruct is better)
        User user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(request.getPassword())
                .role(request.getRole())
                .avatar(request.getAvatar())
                .build();

        return ResponseEntity.ok(userService.registerUser(user));
    }

    @PostMapping("/login")
    public ResponseEntity<com.buy01.user.dto.AuthResponse> login(@RequestBody com.buy01.user.dto.LoginRequest request) {
        return ResponseEntity.ok(userService.login(request));
    }
}
