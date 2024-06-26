package alquileres.persistencia.dto;

public class ReservaDTO {

    private String idBicicleta;
    private String creada;
    private String caducidad;
    private boolean caducada;
    private boolean activa;

    public ReservaDTO() {

    }

    public String getIdBicicleta() {
        return idBicicleta;
    }

    public String getCreada() {
        return creada;
    }

    public String getCaducidad() {
        return caducidad;
    }

    public boolean isCaducada() {
        return caducada;
    }

    public boolean isActiva() {
        return activa;
    }

    public void setIdBicicleta(String idBicicleta) {
        this.idBicicleta = idBicicleta;
    }

    public void setCreada(String creada) {
        this.creada = creada;
    }

    public void setCaducidad(String caducidad) {
        this.caducidad = caducidad;
    }

    public void setCaducada(boolean caducada) {
        this.caducada = caducada;
    }

    public void setActiva(boolean activa) {
        this.activa = activa;
    }
}
