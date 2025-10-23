package TAMBO.dto.controller;

import java.util.Set;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import TAMBO.dto.JWT.JwtService;
import TAMBO.entity.Usuario;
import TAMBO.repository.RolRepository;
import TAMBO.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServices {

        private final UsuarioRepository usuarioRepository;
        private final RolRepository rolRepository;
        private final JwtService jwtService;
        private final PasswordEncoder passwordEncoder;
        private final AuthenticationManager authenticationManager;

        public AuthResponse login(LoginRequest request) {
                authenticationManager.authenticate(
                                new UsernamePasswordAuthenticationToken(request.getCorreo(), request.getContraseña()));

                var usuario = usuarioRepository.findByCorreo(request.getCorreo())
                                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

                var token = jwtService.generateToken(usuario);

                return AuthResponse.builder()
                                .token(token)
                                .mensaje("Login exitoso")
                                .id(usuario.getId())
                                .nombre(usuario.getNombres())
                                .correo(usuario.getCorreo())
                                .rol(usuario.getRoles().iterator().next().getNombre())
                                .build();
        }

        public AuthResponse registro(RegisterRequest request) {
                if (usuarioRepository.existsByCorreo(request.getCorreo())) {
                        throw new RuntimeException("El correo ya está registrado");
                }

                var rolUser = rolRepository.findByNombre("ROLE_USER")
                                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));

                var usuario = Usuario.builder()
                                .nombres(request.getNombres())
                                .apellidos(request.getApellidos())
                                .direccion(request.getDireccion())
                                .telefono(request.getTelefono())
                                .dni(request.getDni())
                                .correo(request.getCorreo())
                                .contraseña(passwordEncoder.encode(request.getContraseña()))
                                .roles(Set.of(rolUser))
                                .build();

                usuarioRepository.save(usuario);

                var token = jwtService.generateToken(usuario);

                return AuthResponse.builder()
                                .token(token)
                                .mensaje("Registro exitoso")
                                .id(usuario.getId())
                                .nombre(usuario.getNombres())
                                .correo(usuario.getCorreo())
                                .rol(rolUser.getNombre())
                                .build();
        }
}
