package alquileres.persistencia.dto;

public class LiberarBloqueoRequest {
    private String idUsuario;

    public LiberarBloqueoRequest() {
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }
}