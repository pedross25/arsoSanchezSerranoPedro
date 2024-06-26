package alquileres.persistencia.dto;
public class EstacionRequest {

    String idEstacion;

    public EstacionRequest() {

    }

    public EstacionRequest(String idBicicleta) {
        this.idEstacion = idBicicleta;
    }

    public String getIdEstacion() {
        return idEstacion;
    }

    public void setIdEstacion(String idEstacion) {
        this.idEstacion = idEstacion;
    }
}
