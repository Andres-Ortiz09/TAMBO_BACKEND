package com.proy.utp.backend_tambo.controller;

import com.proy.utp.backend_tambo.dto.UserResponse;
import com.proy.utp.backend_tambo.model.User;
import com.proy.utp.backend_tambo.repository.UserRepository;
import com.proy.utp.backend_tambo.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.security.core.Authentication;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService service;
    private final UserRepository userRepo;

     public UserController(UserService service, UserRepository userRepo) {
        this.service = service;
        this.userRepo = userRepo; 
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<User>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> create(@Valid @RequestBody User user) {
        return ResponseEntity.ok(service.create(user));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> update(@PathVariable Long id, @Valid @RequestBody User user) {
        return ResponseEntity.ok(service.update(id, user));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    // ENDPOINT PARA USUARIO LOGUEADO
    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser(Authentication authentication) {
        String email = authentication.getName(); // email del usuario logueado
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        UserResponse res = new UserResponse(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getAddress(),
                user.getPhone(),
                user.getPassword(),
                user.getRoles().stream().map(r -> r.getName()).collect(Collectors.toSet())
        );
        return ResponseEntity.ok(res);
    }
    @GetMapping("/public/{id}")
    public ResponseEntity<UserResponse> getClientePublico(@PathVariable Long id) {
        return userRepo.findById(id)
                .map(user -> ResponseEntity.ok(new UserResponse(
                        user.getId(),
                        user.getFirstName(),
                        user.getLastName(),
                        user.getEmail(),
                        user.getAddress(),
                        user.getPhone(),
                        null,
                        null
                )))
                .orElse(ResponseEntity.notFound().build());
    }
}
