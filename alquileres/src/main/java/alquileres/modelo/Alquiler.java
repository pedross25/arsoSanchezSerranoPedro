package alquileres.modelo;

import java.time.Duration;
import java.time.LocalDateTime;

import repositorio.Identificable;

public class Alquiler implements Identificable {

	private String idBicicleta;
	private LocalDateTime inicio;
	private LocalDateTime fin;


	public LocalDateTime getInicio() {
		return inicio;
	}

	public void setInicio(LocalDateTime inicio) {
		this.inicio = inicio;
	}

	public LocalDateTime getFin() {
		return fin;
	}

	public void setFin(LocalDateTime fin) {
		this.fin = fin;
	}

	public boolean isActivo() {
		return fin == null;
	}
	
	public int getTiempo() {
		LocalDateTime finAlquiler = (fin != null) ? fin : LocalDateTime.now();
        Duration duracion = Duration.between(inicio, finAlquiler);
        return (int) duracion.toMinutes();
	}

	@Override
	public String getId() {
		return idBicicleta;
	}

	@Override
	public void setId(String id) {
		this.idBicicleta = id;
		
	}
}
