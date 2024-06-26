package alquileres.servicio;

import repositorio.EntidadNoEncontrada;
import repositorio.OperacionNoPermitida;
import repositorio.RepositorioException;

public interface IServicioAlquiler {

	void reservar(String idUsuario, String idBicicleta) throws RepositorioException, EntidadNoEncontrada, OperacionNoPermitida;

	void confirmarReserva(String idUsuario) throws RepositorioException, EntidadNoEncontrada, OperacionNoPermitida;
	
	void alquilar(String idUsuario, String idBicicleta) throws RepositorioException, EntidadNoEncontrada, OperacionNoPermitida;
	
	UsuarioResumen hitorialUsuario(String idUsuario) throws RepositorioException, EntidadNoEncontrada;
	
	void dejarBicicleta(String idUsuario, String idEstacion) throws RepositorioException, EntidadNoEncontrada, ServicioAlquileresException;
	
	void liberarBloqueo(String idUsuario) throws RepositorioException, EntidadNoEncontrada;
	
	void eliminarReservaActiva(String idBicicleta) throws RepositorioException, EntidadNoEncontrada;
	
}