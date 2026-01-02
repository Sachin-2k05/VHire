package com.example.vHire.dto_Layer.UserDto;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class UserResponseDto {

    private Long id;
    private String Email ;
    private String name ;
    private String role ;
    private String city;
}
