package com.example.vHire.security.Auth;


import lombok.*;

@Data
@Getter
@Setter

@NoArgsConstructor
public class LoginResponseDto {


    private String token;
    private Long userId;
    private String email;
    private String message;
    private String role;

    public LoginResponseDto(String token, Long userId, String email, String message , String role ) {
        this.token = token;
        this.userId = userId;
        this.email = email;
        this.message = "login successful";
        this.role = role;

    }

}
