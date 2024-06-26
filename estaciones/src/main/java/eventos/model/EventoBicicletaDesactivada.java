package eventos.model;

public class EventoBicicletaDesactivada {

    private String idBicicleta;

    public EventoBicicletaDesactivada() {

    }

    public EventoBicicletaDesactivada(String idBicicleta) {
        this.idBicicleta = idBicicleta;
    }

    public String getIdBicicleta() {
        return idBicicleta;
    }

    public void setIdBicicleta(String idBicicleta) {
        this.idBicicleta = idBicicleta;
    }

}
