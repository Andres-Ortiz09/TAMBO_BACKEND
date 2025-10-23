package com.proy.utp.backend_tambo.controller;

import com.proy.utp.backend_tambo.dto.*;
import com.proy.utp.backend_tambo.model.User;
import com.proy.utp.backend_tambo.repository.UserRepository;
import com.proy.utp.backend_tambo.security.JwtTokenProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import com.proy.utp.backend_tambo.service.AuthService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final UserRepository userRepo;

    public AuthController(AuthService authService, AuthenticationManager authenticationManager,
            JwtTokenProvider tokenProvider, UserRepository userRepo) {
        this.authService = authService;
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.userRepo = userRepo;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest req) {
        User u = new User();
        u.setFirstName(req.getFirstName());
        u.setLastName(req.getLastName());
        u.setEmail(req.getEmail());
        u.setPassword(req.getPassword());
        u.setAddress(req.getAddress());
        u.setPhone(req.getPhone());
        UserResponse saved = authService.registerUser(u);
        return ResponseEntity.ok(saved);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(auth);
        String token = tokenProvider.generateToken(auth);
        return ResponseEntity.ok(new AuthResponse(token));
    }
}
