package alquileres.persistencia.jpa.modelo;

import repositorio.Identificable;

import javax.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "reservas")
public class ReservaEntidad implements Identificable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column
	private String idBicicleta;

	@Column(nullable = false)
	private LocalDateTime creada;

	@Column
	private LocalDateTime caducidad;

	@Column
	private String usuarioId;

	@Override
	public String getId() {
		return id.toString();
	}

	@Override
	public void setId(String id) {
		this.id = Long.valueOf(id);
	}

	public void setId(Long id) {
		this.id = id;
	}

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

	public String getUsuarioId() {
		return usuarioId;
	}

	public void setUsuarioId(String usuarioId) {
		this.usuarioId = usuarioId;
	}
}
