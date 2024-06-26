package alquileres.persistencia.dto;

public class AlquilerDTO {
    private String idBicicleta;

    private String inicio;
    private String fin;
    private boolean activo;
    private int tiempo;

    public String getIdBicicleta() {
        return idBicicleta;
    }

    public void setIdBicicleta(String idBicicleta) {
        this.idBicicleta = idBicicleta;
    }

    public String getInicio() {
        return inicio;
    }

    public void setInicio(String inicio) {
        this.inicio = inicio;
    }

    public String getFin() {
        return fin;
    }

    public void setFin(String fin) {
        this.fin = fin;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public int getTiempo() {
        return tiempo;
    }

    public void setTiempo(int tiempo) {
        this.tiempo = tiempo;
    }
}