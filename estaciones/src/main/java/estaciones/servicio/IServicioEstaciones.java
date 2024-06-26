package estaciones.servicio;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import estaciones.modelo.Bicicleta;
import estaciones.modelo.Estacion;
import estaciones.repositorio.EntidadNoEncontrada;
import estaciones.repositorio.RepositorioException;
import estaciones.rest.EstacionResumen;
import estaciones.rest.dto.BicicletaDTO;
import estaciones.rest.dto.EstacionDTO;

public interface IServicioEstaciones {

    // Métodos para estaciones
    String altaEstacion(String nombre, int numPuestos, String direccion, double latitud, double longitud);

    EstacionResumen obtenerEstacion(String idEstacion) throws RepositorioException, EntidadNoEncontrada;

    Page<EstacionDTO> listarEstaciones(Pageable pageable) throws RepositorioException;

    // Métodos para bicicletas
    String altaBicicleta(String modelo, String idEstacion) throws RepositorioException, EntidadNoEncontrada;

    void bajaBicicleta(String idBicicleta, String motivo) throws RepositorioException, EntidadNoEncontrada;
    
    void bajaBicicleta(String idBicicleta, LocalDateTime fecha, String motivo) throws RepositorioException, EntidadNoEncontrada;

    //List<BicicletaDTO> listarBicicletasEstacion(String idEstacion) throws RepositorioException, EntidadNoEncontrada;

    Page<BicicletaDTO> listarBicicletasDisponiblesEstacion(String idEstacion, Pageable pageable) throws RepositorioException, EntidadNoEncontrada;

    void estacionarBicicleta(String idEstacion, String idBicicleta) throws RepositorioException, EntidadNoEncontrada;

    void bicicletaDisponible(String idBicicleta) throws EntidadNoEncontrada;
    
    
    // Paginado
    
    //public Page<EstacionDTO> getListadoPaginado(Pageable pageable);
    
    public Page<BicicletaDTO> getBicicletasEstacionPaginado(String idEstacion, Pageable pageable) throws EntidadNoEncontrada;
}
