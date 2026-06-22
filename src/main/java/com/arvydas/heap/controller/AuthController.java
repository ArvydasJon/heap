package com.arvydas.heap.controller;

import com.arvydas.heap.dto.LoginRequest;
import com.arvydas.heap.dto.RegisterRequest;
import com.arvydas.heap.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public String register(@RequestBody RegisterRequest request) {
        authService.register(request);
        return "User registered successfully";
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @GetMapping("/me")
    public Map<String, Object> me(Authentication authentication) {
        String role = authentication.getAuthorities()
                .stream()
                .findFirst()
                .map(Object::toString)
                .orElse("");

        return Map.of(
                "emailiukas", authentication.getName(),
                "role", role
        );
    }
}
