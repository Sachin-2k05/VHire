package com.example.VHire.Security.Auth;


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
