package estaciones.rest.dto;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import estaciones.modelo.Estacion;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO de la entidad Estacion")
public class EstacionDTO {
	@Schema(description = "Identificador de la estación")
	private String id;
	@Schema(
	        description = "Nombre de la estación",
	        example = "Estación Central"
	    )
	@NotNull
	private String nombre;
	@Schema(
	        description = "Dirección de la estación",
	        example = "Calle Ejemplo 123"
	    )
	@NotNull
	private String direccion;
	@Schema(
	        description = "Latitud de la ubicación de la estación",
	        example = "40.712776"
	    )
	@NotNull
	private Double latitud;
	@Schema(
	        description = "Longitud de la ubicación de la estación",
	        example = "-74.005974"
	    )
	@NotNull
	private Double longitud;
	@Schema(
	        description = "Número de puestos de la estación",
	        example = "20"
	    )
	@NotNull
	private int puestos;
	@Schema(
	        description = "Fecha de alta de la estación",
	        example = "2022-01-01T12:00:00"
	    )
	@NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	private String fechaAlta;
	@Schema(
	    )
	private List<BicicletaDTO> bicicletas;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public Double getLatitud() {
		return latitud;
	}

	public void setLatitud(Double latitud) {
		this.latitud = latitud;
	}

	public Double getLongitud() {
		return longitud;
	}

	public void setLongitud(Double longitud) {
		this.longitud = longitud;
	}

	public int getPuestos() {
		return puestos;
	}

	public void setPuestos(int puestos) {
		this.puestos = puestos;
	}

	public String getFechaAlta() {
		return fechaAlta;
	}

	public void setFechaAlta(String fechaAlta) {
		this.fechaAlta = fechaAlta;
	}

	public List<BicicletaDTO> getBicicletas() {
		return bicicletas;
	}

	public void setBicicletas(List<BicicletaDTO> bicicletas) {
		this.bicicletas = bicicletas;
	}
	
	public static EstacionDTO fromEntity(Estacion estacion) {
        EstacionDTO dto = new EstacionDTO();
        dto.setId(estacion.getId());
        dto.setNombre(estacion.getNombre());
        dto.setDireccion(estacion.getDireccion());
        dto.setLatitud(estacion.getLatitud());
        dto.setLongitud(estacion.getLongitud());
        dto.setPuestos(estacion.getPuestos());
        dto.setFechaAlta(estacion.getFechaAlta().toString());

        List<BicicletaDTO> bicicletas = estacion.getBicicletas().stream()
                .map(BicicletaDTO::fromEntity)
                .collect(Collectors.toList());
        dto.setBicicletas(bicicletas);

        return dto;
    }
}
