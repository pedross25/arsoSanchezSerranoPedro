package model;

public class UserDetails {
    private String userId;
    private String name;
    private String rol;

    public UserDetails() {
    }

    public UserDetails(String userId, String name, String rol) {
        this.userId = userId;
        this.name = name;
        this.rol = rol;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
}