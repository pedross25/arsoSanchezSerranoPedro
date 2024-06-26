package alquileres.modelo;

import java.time.LocalDateTime;
import java.util.Objects;

public class Reserva {

	private String idBicicleta;
	private LocalDateTime creada;
	private LocalDateTime caducidad;

	public String getIdBicicleta() {
		return idBicicleta;
	}

	public void setIdBicicleta(String idBicicleta) {
		this.idBicicleta = idBicicleta;
	}

	public LocalDateTime getCreada() {
		return creada;
	}

	public void setCreada(LocalDateTime creada) {
		this.creada = creada;
	}

	public LocalDateTime getCaducidad() {
		return caducidad;
	}

	public void setCaducidad(LocalDateTime caducidad) {
		this.caducidad = caducidad;
	}

	public boolean isCaducada() {
		return LocalDateTime.now().isAfter(caducidad);
	}

	public boolean isActiva() {
		return !isCaducada();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Reserva reserva = (Reserva) o;
		return Objects.equals(idBicicleta, reserva.idBicicleta) && Objects.equals(creada, reserva.creada)
				&& Objects.equals(caducidad, reserva.caducidad);
	}

	@Override
	public int hashCode() {
		return Objects.hash(idBicicleta, creada, caducidad);
	}

}
