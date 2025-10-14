package TAMBO.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

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

    public UsuarioDTO(TAMBO.entity.Usuario usuario) {
        this.nombres = usuario.getNombres();
        this.apellidos = usuario.getApellidos();
        this.direccion = usuario.getDireccion();
        this.telefono = usuario.getTelefono();
        this.dni = usuario.getDni();
        this.correo = usuario.getCorreo();
        this.contraseña = "";
    }
}
