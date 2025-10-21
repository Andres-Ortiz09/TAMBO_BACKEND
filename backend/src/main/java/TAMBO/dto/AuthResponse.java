package TAMBO.dto;

public class AuthResponse {
    private String token;
    private String rol;

    public AuthResponse(String token, String rol) {
        this.token = token;
        this.rol = rol;
    }

    public String getToken() {
        return token;
    }

    public String getRol() {
        return rol;
    }
}
