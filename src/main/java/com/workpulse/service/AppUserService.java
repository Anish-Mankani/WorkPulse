package com.workpulse.service;

import com.workpulse.dto.RegisterRequest;
import com.workpulse.dto.UserResponse;
import com.workpulse.model.AppUser;
import com.workpulse.model.UserRole;
import com.workpulse.repository.AppUserRepository;
import com.workpulse.util.JwtUtil;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Service
public class AppUserService {

    private final JwtUtil jwtUtil;
    private AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;


    private UserResponse convertToResponse(AppUser user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole());
        return response;
    }


    public UserResponse registerUser(RegisterRequest request) {
        if (appUserRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("This email is already registered : " + request.getEmail());
        }
        AppUser user = new AppUser();
        user.setName(request.getName());
        user.setEmail(request.getEmail());

        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(UserRole.USER);
        return convertToResponse(appUserRepository.save(user));
    }


    public String loginUser(String email, String password) {

         authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );
        AppUser user = appUserRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return jwtUtil.generateToken(user);
    }

    public UserResponse getUserById(UUID userId) {
        AppUser user = appUserRepository.findById(userId)
            .orElseThrow(()->new RuntimeException("User not found with ID: " + userId));
        return convertToResponse(user);
    }

    public List<UserResponse> getAllUser() {
        return appUserRepository.findAll()
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    public void deleteUser(UUID userId) {
        AppUser user = appUserRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        appUserRepository.delete(user);
    }



    public UserResponse updateUserRole(UUID id, UserRole role) {
        AppUser user = appUserRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setRole(role);
        AppUser updatedUser = appUserRepository.save(user);
        return convertToResponse(updatedUser);


    }
}
