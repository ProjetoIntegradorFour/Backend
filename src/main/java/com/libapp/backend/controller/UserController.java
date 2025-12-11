// src/main/java/com/libapp/backend/controller/TestController.java
package com.libapp.backend.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.libapp.backend.dto.ProfileResponse;
import com.libapp.backend.security.UserDetailsImpl;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @GetMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ProfileResponse> profile(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        // Extract roles from authorities
        List<String> roles = userDetails.getAuthorities().stream()
                .map(authority -> authority.getAuthority())
                .collect(Collectors.toList());

        // Create the profile response
        ProfileResponse response = new ProfileResponse(
                userDetails.getId(),
                userDetails.getUsername(), // This is the name in your UserDetailsImpl
                userDetails.getCpf(),
                roles);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/admin/all")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<String> all() {
        return ResponseEntity.ok("You are an admin and can see all users");
    }

    @PutMapping("/admin/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<String> update() {
        return ResponseEntity.ok("You are an admin and can update user data");
    }

    @GetMapping("/admin/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<String> seeone() {
        return ResponseEntity.ok("You are an admin and can see a specific user data");
    }

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("User controller is working");
    }
}