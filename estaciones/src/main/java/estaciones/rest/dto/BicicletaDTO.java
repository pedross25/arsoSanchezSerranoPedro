package estaciones.rest.dto;


import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import estaciones.modelo.Bicicleta;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO de la entidad Bicicleta")
public class BicicletaDTO {
	@Schema(description = "Identificador de la bicicleta")
	private String id;
	@Schema(description = "Modelo de la bicicleta", example = "Mountain Bike")
	@NotNull
	private String modelo;
	@Schema(description = "Fecha de alta de la bicicleta", example = "2022-01-01T12:00:00Z")
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	private String fechaAlta;
	@Schema(description = "Fecha de baja de la bicicleta", example = "2023-06-01T12:00:00Z")
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	private String fechaBaja;
	@Schema(description = "Motivo de la baja de la bicicleta", example = "Reparación necesaria")
	private String motivoBaja;
	@Schema(description = "Disponibilidad de la bicicleta", example = "true")
	private boolean disponible;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getModelo() {
		return modelo;
	}

	public void setModelo(String modelo) {
		this.modelo = modelo;
	}

	public String getFechaAlta() {
		return fechaAlta;
	}

	public void setFechaAlta(String fechaAlta) {
		this.fechaAlta = fechaAlta;
	}

	public String getFechaBaja() {
		return fechaBaja;
	}

	public void setFechaBaja(String fechaBaja) {
		this.fechaBaja = fechaBaja;
	}

	public String getMotivoBaja() {
		return motivoBaja;
	}

	public void setMotivoBaja(String motivoBaja) {
		this.motivoBaja = motivoBaja;
	}

	public boolean isDisponible() {
		return disponible;
	}

	public void setDisponible(boolean disponible) {
		this.disponible = disponible;
	}

	public static BicicletaDTO fromEntity(Bicicleta bicicleta) {
		BicicletaDTO dto = new BicicletaDTO();
		dto.setId(bicicleta.getId());
		dto.setModelo(bicicleta.getModelo());
		dto.setFechaAlta(bicicleta.getFechaAlta().toString());
		dto.setFechaBaja(bicicleta.getFechaBaja().toString());
		dto.setMotivoBaja(bicicleta.getMotivoBaja());
		dto.setDisponible(bicicleta.isDisponible());
		return dto;
	}

}
