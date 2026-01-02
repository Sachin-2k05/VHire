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

    public LoginResponseDto(String token, Long userId, String email) {
        this.token = token;
        this.userId = userId;
        this.email = email;
    }

}
