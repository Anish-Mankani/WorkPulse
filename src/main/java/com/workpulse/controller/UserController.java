package com.workpulse.controller;

import com.workpulse.dto.LoginRequest;
import com.workpulse.dto.RegisterRequest;
import com.workpulse.dto.UserResponse;
import com.workpulse.model.UserRole;
import com.workpulse.service.AppUserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/api/user")
@AllArgsConstructor
public class UserController {

    private AppUserService appUserService;

    @PostMapping("/register")
    public ResponseEntity <UserResponse> registerUser(@RequestBody RegisterRequest registration ) {
        UserResponse userResponse = appUserService.registerUser(registration);
        return ResponseEntity.status(HttpStatus.CREATED).body(userResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody LoginRequest loginRequest) {
        String token = appUserService.loginUser(loginRequest.getEmail(), loginRequest.getPassword());
        return ResponseEntity.ok(token);
    }


    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity <List<UserResponse>> getAllUser() {
        List<UserResponse> users = appUserService.getAllUser();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable UUID id) {
        UserResponse user = appUserService.getUserById(id);
        return ResponseEntity.ok(user);

    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity <Void> deleteUser (@PathVariable UUID id) {
         appUserService.deleteUser(id);
         return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/role")
    public ResponseEntity<UserResponse> updateUserRole(
            @PathVariable UUID id,
            @RequestParam UserRole role
    ) {
        UserResponse updatedUser = appUserService.updateUserRole(id, role);
        return ResponseEntity.ok(updatedUser);
    }
}

