package com.example.socialmedia.controller;

import com.example.socialmedia.dto.AuthResponse;
import com.example.socialmedia.dto.MessageResponse;
import com.example.socialmedia.dto.LoginRequest;
import com.example.socialmedia.dto.RegisterRequest;
import com.example.socialmedia.service.AuthService;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<MessageResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(new MessageResponse(authService.register(request)));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyEmail(@RequestBody Map<String, String> body) {
        String token = body.get("token");
        return ResponseEntity.ok(new MessageResponse(authService.verifyAccount(token)));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        return ResponseEntity.ok(new MessageResponse(authService.forgotPassword(email)));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> body) {
        String token = body.get("token");
        String newPassword = body.get("newPassword");
        return ResponseEntity.ok(new MessageResponse(authService.resetPassword(token, newPassword)));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(org.springframework.security.core.Authentication authentication) {
        return ResponseEntity.ok(authService.refreshToken(authentication.getName()));
    }
}
