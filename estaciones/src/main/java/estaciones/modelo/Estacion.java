package estaciones.modelo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "estaciones")
public class Estacion {
	@Id
	private String id;
	private String nombre;
	private String direccion;
	private Double latitud;
	private Double longitud;
	private int puestos;
	private LocalDateTime fechaAlta;
	
	@DBRef
    private List<Bicicleta> bicicletas = new ArrayList<>();

	public Estacion() {
		super();
	}

	public Estacion(String nombre, int numeroPuestos, String direccion, double latitud, double longitud,
			LocalDateTime fechaAlta) {
		this.nombre = nombre;
		this.puestos = numeroPuestos;
		this.direccion = direccion;
		this.latitud = latitud;
		this.longitud = longitud;
		this.fechaAlta = fechaAlta;
		this.bicicletas = new LinkedList<>();
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

	public LocalDateTime getFechaAlta() {
		return fechaAlta;
	}

	public void setFechaAlta(LocalDateTime fechaAlta) {
		this.fechaAlta = fechaAlta;
	}

	public String getId() {
		return id;
	}

	public int getCapacidadMaxima() {
		return puestos;
	}

	public int getHuecosDisponibles() {
		return puestos - bicicletas.size();
	}

	public boolean estacionamientoDisponible() {
		return bicicletas.size() < puestos;
	}

	public void agregarBicicleta(Bicicleta bicicleta) {
		if (estacionamientoDisponible()) {
			bicicletas.add(bicicleta);
		} else {
			throw new IllegalStateException("No hay espacio disponible en la estación");
		}
	}
	
	public void eliminarBicicleta(Bicicleta bicicleta) {
		bicicletas.remove(bicicleta);
	}

	public List<Bicicleta> getBicicletas() {
		return bicicletas;
	}

	public void setBicicletas(List<Bicicleta> bicicletas) {
		this.bicicletas = bicicletas;
	}
	
	public List<Bicicleta> getBicicletasDisponibles() {
	    return bicicletas.stream()
	                     .filter(Bicicleta::isDisponible)
	                     .collect(Collectors.toList());
	}
	
}