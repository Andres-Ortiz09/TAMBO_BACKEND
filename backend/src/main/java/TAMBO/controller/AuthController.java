package TAMBO.controller;

import TAMBO.dto.AuthRequest;
import TAMBO.dto.AuthResponse;
import TAMBO.dto.UsuarioDTO;
import TAMBO.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<UsuarioDTO> register(@RequestBody UsuarioDTO dto) {
        UsuarioDTO registrado = authService.register(dto);
        return ResponseEntity.ok(registrado);
    }
}
