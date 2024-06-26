package alquileres.servicio;


public interface IServicioEstaciones {
	
	boolean isHuecoDisponible(String idEstacion) throws ServicioAlquileresException;
	boolean situarBicicleta(String idEstacion, String idBicicleta) throws ServicioAlquileresException;
}
