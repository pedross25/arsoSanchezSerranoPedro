package model;

public class AuthResponse {
    private String token;
    private String idUser;
    private String nombre;
    private String rol;

    public AuthResponse() {
    }

    public AuthResponse(String token, String idUser, String nombre, String rol) {
        this.token = token;
        this.idUser = idUser;
        this.nombre = nombre;
        this.rol = rol;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
}