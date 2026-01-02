package com.example.vHire.security.Auth;


import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {


    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;

    }

    @PostMapping("/signup")
    public ResponseEntity<SignUpResponseDto> signup(  @Valid  @RequestBody SignUpRequestDto signUpRequestDto) {
        return ResponseEntity.
                status(HttpStatus.OK)
                .body(authService.signup(signUpRequestDto));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@Valid  @RequestBody LoginRequestDto loginRequestDto) {
        return ResponseEntity.
                ok(authService.login(loginRequestDto));

    }
}
