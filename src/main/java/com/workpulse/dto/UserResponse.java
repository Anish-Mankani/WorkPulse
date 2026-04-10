package com.workpulse.dto;

import com.workpulse.model.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse{
    private UUID id;
    private String name;
    private String email;
    private UserRole role;

}
