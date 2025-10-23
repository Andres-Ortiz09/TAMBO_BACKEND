package TAMBO.dto.controller;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
    private String token;
    private String mensaje;
    private Long id;
    private String nombre;
    private String correo;
    private String rol;
}
