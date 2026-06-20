package com.cafepos.controller;

import com.cafepos.dto.request.LoginRequest;
import com.cafepos.dto.request.SignupRequest;
import com.cafepos.dto.response.AuthResponse;
import com.cafepos.dto.response.UserResponse;
import com.cafepos.entity.User;
import com.cafepos.entity.UserRole;
import com.cafepos.entity.UserStatus;
import com.cafepos.exception.ConflictException;
import com.cafepos.exception.ForbiddenException;
import com.cafepos.repository.UserRepository;
import com.cafepos.security.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signup(@Valid @RequestBody SignupRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new ConflictException("Email is already registered");
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .role(UserRole.ADMIN) // Defaults to admin per spec
                .status(UserStatus.ACTIVE)
                .build();

        User savedUser = userRepository.save(user);
        String token = jwtUtil.generateToken(savedUser);

        UserResponse userResponse = UserResponse.builder()
                .id(savedUser.getId())
                .name(savedUser.getName())
                .email(savedUser.getEmail())
                .role(savedUser.getRole())
                .status(savedUser.getStatus())
                .createdAt(savedUser.getCreatedAt())
                .build();

        return ResponseEntity.ok(new AuthResponse(token, userResponse));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadCredentialsException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new BadCredentialsException("Invalid email or password");
        }

        if (user.getStatus() == UserStatus.ARCHIVED) {
            throw new ForbiddenException("User account is archived");
        }

        String token = jwtUtil.generateToken(user);

        UserResponse userResponse = UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .status(user.getStatus())
                .createdAt(user.getCreatedAt())
                .build();

        return ResponseEntity.ok(new AuthResponse(token, userResponse));
    }
}
