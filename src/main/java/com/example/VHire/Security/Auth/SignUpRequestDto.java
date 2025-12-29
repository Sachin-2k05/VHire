package com.example.VHire.Security.Auth;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@AllArgsConstructor
public class SignUpRequestDto {


    private String name;
    private String email;
    private String password;
    private String role;
}
