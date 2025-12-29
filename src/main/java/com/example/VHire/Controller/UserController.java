package com.example.VHire.Controller;


import com.example.VHire.DTO_Layer.UserDto.UserResponseDto;
import com.example.VHire.Entity.User;
import com.example.VHire.Service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    public ResponseEntity<UserResponseDto> GetUserById(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.getUserByIdAsDto(userId));

    }

    @GetMapping("/myProfile")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserResponseDto> GetCurrentUser() {
        User currentUser = getcurrentUser() ;

        return ResponseEntity.ok(userService.mapToUserResponse(currentUser));
    }
    private User getcurrentUser() {
        throw new UnsupportedOperationException("Not supported yet.");

    }
}
