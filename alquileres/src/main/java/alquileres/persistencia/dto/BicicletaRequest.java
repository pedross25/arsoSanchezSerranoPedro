package alquileres.persistencia.dto;

public class BicicletaRequest {

    String idBicicleta;

    public BicicletaRequest() {

    }

    public BicicletaRequest(String idBicicleta) {
        this.idBicicleta = idBicicleta;
    }

    public String getIdBicicleta() {
        return idBicicleta;
    }

    public void setIdBicicleta(String idBicicleta) {
        this.idBicicleta = idBicicleta;
    }
}
