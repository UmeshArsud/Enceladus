package com.enceladus.enceladus.controller;

import com.enceladus.enceladus.dto.ApiResponse;
import com.enceladus.enceladus.dto.SignInRequest;
import com.enceladus.enceladus.dto.SignInResponce;
import com.enceladus.enceladus.dto.SignUpRequest;
import com.enceladus.enceladus.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService){
        this.authService = authService;
    }
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        authService.registerUser(signUpRequest);
        return ResponseEntity.ok(new ApiResponse(true, "User Registered Successfully"));
    }
    @PostMapping("/signin")
    public ResponseEntity<SignInResponce> authenticateUser(@Valid @RequestBody SignInRequest signInRequest) {
        signInResponse signInResponse = authService.authenticateUser(signInRequest);
        return ResponseEntity.ok(signInResponse);
    }
}
