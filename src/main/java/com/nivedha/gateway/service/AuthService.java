package com.nivedha.gateway.service;

import com.nivedha.gateway.controller.AuthController.AuthRequest;
import com.nivedha.gateway.controller.AuthController.AuthResponse;
import com.nivedha.gateway.model.User;
import com.nivedha.gateway.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(AuthRequest request) {
        var user = new User();
        user.setUsername(request.username());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(User.Role.USER);
        userRepository.save(user);
        var token = jwtService.generateToken(
                new org.springframework.security.core.userdetails.User(
                        user.getUsername(), user.getPassword(), java.util.List.of()
                )
        );
        return new AuthResponse(token);
    }

    public AuthResponse login(AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );
        var user = userRepository.findByUsername(request.username()).orElseThrow();
        var token = jwtService.generateToken(
                new org.springframework.security.core.userdetails.User(
                        user.getUsername(), user.getPassword(), java.util.List.of()
                )
        );
        return new AuthResponse(token);
    }
}
