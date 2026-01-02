package com.example.vHire.controller;


import com.example.vHire.dto_Layer.UserDto.UserResponseDto;
import com.example.vHire.entity.User;
import com.example.vHire.service.UserService;
import jakarta.annotation.PostConstruct;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    UserController(UserService userService) {
        this.userService = userService;

    }

    @GetMapping("/{userId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserResponseDto> GetUserById(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.getUserByIdAsDto(userId));

    }

    @GetMapping("/myProfile")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserResponseDto> GetCurrentUser(@AuthenticationPrincipal User worker) {


        return ResponseEntity.ok(userService.mapToUserResponse(worker));
    }




}
