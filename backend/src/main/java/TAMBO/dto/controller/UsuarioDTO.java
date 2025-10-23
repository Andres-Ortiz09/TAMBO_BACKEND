package TAMBO.dto.controller;

import java.util.Set;
import java.util.stream.Collectors;

import TAMBO.entity.Rol;
import TAMBO.entity.Usuario;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UsuarioDTO {
    private String nombres;
    private String apellidos;
    private String direccion;
    private String telefono;
    private String dni;
    private String correo;
    private String contraseña;

    private Set<String> roles;

    public UsuarioDTO(Usuario usuario) {
        this.nombres = usuario.getNombres();
        this.apellidos = usuario.getApellidos();
        this.direccion = usuario.getDireccion();
        this.telefono = usuario.getTelefono();
        this.dni = usuario.getDni();
        // la entidad todavía expone getCorreo()
        this.correo = usuario.getCorreo();
        this.contraseña = "";

        this.roles = usuario.getRoles().stream()
                .map(Rol::getNombre)
                .collect(Collectors.toSet());
    }

    public UsuarioDTO(String nombres, String apellidos, String direccion, String telefono,
            String dni, String correo, String contraseña, Set<String> roles) {
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.direccion = direccion;
        this.telefono = telefono;
        this.dni = dni;
        this.correo = correo;
        this.contraseña = contraseña;
        this.roles = roles;
    }
}
