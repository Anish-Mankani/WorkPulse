package com.workpulse.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LoginRequest {

    private String email;
    private String password;


}
