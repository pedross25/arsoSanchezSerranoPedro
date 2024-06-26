package model;

import javax.validation.constraints.NotBlank;

public class LoginRequest {
    @NotBlank(message = "El nombre de usuario no puede estar en blanco")
    private String user;
    @NotBlank(message = "La contraseña no puede estar en blanco")
    private String password;

    public LoginRequest() {
    }

    public LoginRequest(String username, String password) {
        this.user = username;
        this.password = password;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}