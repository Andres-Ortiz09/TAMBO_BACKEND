package com.proy.utp.backend_tambo.controller;

import com.proy.utp.backend_tambo.dto.UserResponse;
import com.proy.utp.backend_tambo.model.User;
import com.proy.utp.backend_tambo.repository.UserRepository;
import com.proy.utp.backend_tambo.service.UserService;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
@Tag(name = "Usuarios", description = "API para gestionar usuarios")
public class UserController {

    private final UserService service;
    private final UserRepository userRepo;

    public UserController(UserService service, UserRepository userRepo) {
        this.service = service;
        this.userRepo = userRepo;
    }

    @Operation(summary = "Obtener todos los usuarios", description = "Retorna la lista de todos los usuarios (solo ADMIN)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de usuarios obtenida correctamente"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<User>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @Operation(summary = "Obtener usuario por ID", description = "Retorna un usuario específico por su ID (solo ADMIN)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario encontrado correctamente"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @Operation(summary = "Crear un usuario", description = "Crea un nuevo usuario (solo ADMIN)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario creado correctamente"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> create(@Valid @RequestBody User user) {
        return ResponseEntity.ok(service.create(user));
    }

    @Operation(summary = "Actualizar un usuario", description = "Actualiza un usuario existente (solo ADMIN)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario actualizado correctamente"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> update(@PathVariable Long id, @Valid @RequestBody User user) {
        return ResponseEntity.ok(service.update(id, user));
    }

    @Operation(summary = "Eliminar un usuario", description = "Elimina un usuario por su ID (solo ADMIN)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Usuario eliminado correctamente"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Obtener usuario logueado", description = "Retorna los datos del usuario actualmente autenticado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario logueado obtenido correctamente"),
        @ApiResponse(responseCode = "401", description = "Usuario no autenticado")
    })
    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser(Authentication authentication) {
        String email = authentication.getName();
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
}
