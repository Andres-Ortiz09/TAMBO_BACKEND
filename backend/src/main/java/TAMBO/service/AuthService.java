package TAMBO.service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import TAMBO.dto.JWT.JwtService;
import TAMBO.dto.controller.AuthRequest;
import TAMBO.dto.controller.AuthResponse;
import TAMBO.dto.controller.UsuarioDTO;
import TAMBO.entity.Rol;
import TAMBO.entity.Usuario;
import TAMBO.repository.RolRepository;
import TAMBO.repository.UsuarioRepository;

@Service
public class AuthService {

    @Autowired
    private UsuarioRepository repo;

    @Autowired
    private RolRepository rolRepo;

    @Autowired
    private JwtService jwtService;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public UsuarioDTO register(UsuarioDTO dto) {
        if (repo.existsByCorreo(dto.getCorreo())) {
            throw new RuntimeException("Correo ya registrado");
        }

        Usuario usuario = new Usuario();
        usuario.setNombres(dto.getNombres());
        usuario.setApellidos(dto.getApellidos());
        usuario.setCorreo(dto.getCorreo());
        usuario.setContraseña(encoder.encode(dto.getContraseña()));
        usuario.setDni(dto.getDni());
        usuario.setDireccion(dto.getDireccion());
        usuario.setTelefono(dto.getTelefono());

        Set<String> nombresRoles = (dto.getRoles() == null || dto.getRoles().isEmpty())
                ? Set.of("ROLE_USER")
                : dto.getRoles();

        Set<Rol> rolesAsignados = nombresRoles.stream()
                .map(nombre -> rolRepo.findByNombre(nombre)
                        .orElseThrow(() -> new RuntimeException("Rol no encontrado: " + nombre)))
                .collect(Collectors.toSet());

        usuario.setRoles(rolesAsignados);

        Usuario guardado = repo.save(usuario);

        return new UsuarioDTO(
                guardado.getNombres(),
                guardado.getApellidos(),
                guardado.getDireccion(),
                guardado.getTelefono(),
                guardado.getDni(),
                guardado.getCorreo(),
                null,
                nombresRoles);
    }

    public AuthResponse login(AuthRequest request) {
        Optional<Usuario> usuarioOpt = repo.findByCorreo(request.getCorreo());

        if (usuarioOpt.isEmpty()) {
            throw new RuntimeException("Usuario no encontrado");
        }

        Usuario usuario = usuarioOpt.get();

        if (!encoder.matches(request.getContraseña(), usuario.getContraseña())) {
            throw new RuntimeException("Contraseña incorrecta");
        }

        String token = jwtService.generateToken(usuario);
        String rol = usuario.getRoles().stream()
                .findFirst()
                .map(Rol::getNombre)
                .orElse("ROLE_USER");

        return new AuthResponse(
                token,
                "Login exitoso",
                usuario.getId(),
                usuario.getNombres(),
                usuario.getCorreo(),
                rol);
    }
}
