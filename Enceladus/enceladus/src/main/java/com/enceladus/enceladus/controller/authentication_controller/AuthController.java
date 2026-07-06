package com.enceladus.enceladus.controller.authentication_controller;

import com.enceladus.enceladus.dto.ApiResponse;
import com.enceladus.enceladus.dto.authentication_dto.SignInRequest;
import com.enceladus.enceladus.dto.authentication_dto.SignInResponce;
import com.enceladus.enceladus.dto.authentication_dto.SignUpRequest;
import com.enceladus.enceladus.service.jwt_service.AuthService;
import com.enceladus.enceladus.service.jwt_service.TokenBlacklistService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final TokenBlacklistService tokenBlacklistService;

    public AuthController(AuthService authService, TokenBlacklistService tokenBlacklistService) {
        this.authService = authService;
        this.tokenBlacklistService = tokenBlacklistService;
    }

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        authService.registerUser(signUpRequest);
        return ResponseEntity.ok(new ApiResponse(true, "User Registered Successfully"));
    }

    @PostMapping("/signin")
    public ResponseEntity<SignInResponce> authenticateUser(@Valid @RequestBody SignInRequest signInRequest) {
        SignInResponce signInResponse = authService.authenticateUser(signInRequest);
        return ResponseEntity.ok(signInResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse> logout(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse(false, "No Bearer token supplied"));
        }
        String token = authHeader.substring(7).trim();
        tokenBlacklistService.blacklist(token);
        return ResponseEntity.ok(new ApiResponse(true, "Logged out successfully"));
    }
}
