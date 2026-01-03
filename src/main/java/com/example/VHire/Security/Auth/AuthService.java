package com.example.vHire.security.Auth;


import com.example.vHire.entity.Role;
import com.example.vHire.entity.User;
import com.example.vHire.repository.UserRepository;
import com.example.vHire.security.CustomUserDetail;
import com.example.vHire.security.JWT.JwtTokenProvider;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor

public class AuthService {
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;




    public SignUpResponseDto signup(SignUpRequestDto request) {
        if(userRepository.existsByEmail(request.getEmail())) {
            throw new UsernameNotFoundException("Email already in use");

        }


        User user  = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.valueOf(request.getRole()));
        user.setCity(request.getCity());
        userRepository.save(user);

        return new SignUpResponseDto(
                user.getId(),
                " User registered successfully"
        );
    }


    public LoginResponseDto login(LoginRequestDto request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()

                )
        );

        CustomUserDetail userDetails =
                (CustomUserDetail) authentication.getPrincipal();
        String token = jwtTokenProvider.generateToken((userDetails));
        return new LoginResponseDto(
                token ,
                userDetails.getUser().getId()
                ,
                userDetails.getUser().getEmail()
        );
    }


}

