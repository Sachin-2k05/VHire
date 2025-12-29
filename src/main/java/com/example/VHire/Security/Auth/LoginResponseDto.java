package com.example.VHire.Security.Auth;


import lombok.*;

@Data
@Getter
@Setter

@NoArgsConstructor
public class LoginResponseDto {


    private String token;
    private Long userId;
    private String role;

    public LoginResponseDto(String token, Long userId, String role) {
        this.token = token;
        this.userId = userId;
        this.role = role;
    }

}
