// src/main/java/com/libapp/backend/controller/TestController.java
package com.libapp.backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.libapp.backend.dto.CurrentUserSummaryDTO;
import com.libapp.backend.dto.UserUpdateRequestDTO;
import com.libapp.backend.entity.User;
import com.libapp.backend.security.UserDetailsImpl;
import com.libapp.backend.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UsertController {

    private final UserService userService;

    public UsertController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CurrentUserSummaryDTO> userAccess(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        CurrentUserSummaryDTO userSummary = userService.getCurrentUserSummary(userDetails.getId());
        return ResponseEntity.ok(userSummary);
    }

    @GetMapping("/admin/profile")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> adminAccess(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userService.findUserById(userDetails.getId());
        return ResponseEntity.ok(user);
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> updateUser(@PathVariable Long id,
            @RequestBody UserUpdateRequestDTO updateRequest) {
        User updatedUser = userService.updateUser(id, updateRequest);
        return ResponseEntity.ok(updatedUser);
    }

    @GetMapping("/test/all")
    public String allAccess() {
        return "Public Content.";
    }
}