package com.example.vHire.security.Auth;


import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SignUpResponseDto {

    private Long userId;
    private String message;
}
