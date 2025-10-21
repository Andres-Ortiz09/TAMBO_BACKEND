package TAMBO.service;

import TAMBO.dto.AuthRequest;
import TAMBO.dto.AuthResponse;
import TAMBO.dto.UsuarioDTO;
import TAMBO.entity.Usuario;
import TAMBO.entity.Rol;
import TAMBO.repository.UsuarioRepository;
import TAMBO.repository.RolRepository;
import TAMBO.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthService {

    @Autowired
    private UsuarioRepository repo;

    @Autowired
    private RolRepository rolRepo;

    @Autowired
    private JwtUtil jwtUtil;

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
                nombresRoles
        );
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

        String token = jwtUtil.generarToken(usuario.getCorreo());
        String rol = usuario.getRoles().stream()
                .findFirst()
                .map(Rol::getNombre)
                .orElse("ROLE_USER");

        return new AuthResponse(token, rol);
    }
}
