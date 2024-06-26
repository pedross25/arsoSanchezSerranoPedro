package eventos.model;

public class EventoBicicletaAlquilada {

	private String idBicicleta;
	private String fecha;

	public EventoBicicletaAlquilada() {
	}

	public EventoBicicletaAlquilada(String idBicicleta, String fecha) {
		this.idBicicleta = idBicicleta;
		this.fecha = fecha;
	}

	public String getIdBicicleta() {
		return idBicicleta;
	}

	public void setIdBicicleta(String idBicicleta) {
		this.idBicicleta = idBicicleta;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

}
