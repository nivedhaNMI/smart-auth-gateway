package com.nivedha.gateway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ResourceController {

    @GetMapping("/public/hello")
    public ResponseEntity<String> publicEndpoint() {
        return ResponseEntity.ok("This endpoint is public — no token needed.");
    }

    @GetMapping("/user/profile")
    public ResponseEntity<String> userProfile(Authentication authentication) {
        return ResponseEntity.ok("Hello, " + authentication.getName() + "! This is your profile.");
    }

    @GetMapping("/admin/dashboard")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> adminDashboard() {
        return ResponseEntity.ok("Welcome to the admin dashboard.");
    }
}
