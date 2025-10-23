package TAMBO.dto.controller;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    @NotBlank
    private String nombres;
    @NotBlank
    private String apellidos;
    private String direccion;
    @Pattern(regexp = "\\d{9}")
    private String telefono;
    @Pattern(regexp = "\\d{8}")
    private String dni;
    @NotBlank
    @Email
    private String correo;
    @NotBlank
    @Size(min = 6)
    private String contraseña;
}
