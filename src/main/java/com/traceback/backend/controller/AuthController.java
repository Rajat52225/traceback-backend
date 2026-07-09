package com.traceback.backend.controller;

import com.traceback.backend.dto.RegisterRequest;
import com.traceback.backend.model.User;
import com.traceback.backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.traceback.backend.dto.UserResponse;
import com.traceback.backend.dto.LoginRequest;
import com.traceback.backend.dto.LoginResponse;
import com.traceback.backend.service.JwtService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final JwtService jwtService;

    public AuthController(UserService userService,
                          JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(
            @Valid @RequestBody RegisterRequest request) {

        User savedUser = userService.registerUser(request);

        UserResponse response = new UserResponse(
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getEmail()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request) {

        User user = userService.loginUser(request);

        String token = jwtService.generateToken(user.getEmail());

        LoginResponse response = new LoginResponse(token);

        return ResponseEntity.ok(response);
    }
}