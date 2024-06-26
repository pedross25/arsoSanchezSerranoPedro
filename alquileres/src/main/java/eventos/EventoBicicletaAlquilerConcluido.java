package eventos;

public class EventoBicicletaAlquilerConcluido {

	private String idEstacion;
	private String idBicicleta;

	public EventoBicicletaAlquilerConcluido() {
	}

	public EventoBicicletaAlquilerConcluido(String idEstacion, String idBicicleta) {
		this.idEstacion = idEstacion;
		this.idBicicleta = idBicicleta;
	}

	public String getIdEstacion() {
		return idEstacion;
	}

	public void setIdEstacion(String idEstacion) {
		this.idEstacion = idEstacion;
	}

	public String getIdBicicleta() {
		return idBicicleta;
	}

	public void setIdBicicleta(String idBicicleta) {
		this.idBicicleta = idBicicleta;
	}

}
