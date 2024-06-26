package estaciones.servicio;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import eventos.model.EventoBicicletaDesactivada;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import estaciones.modelo.Bicicleta;
import estaciones.modelo.Estacion;
import estaciones.repositorio.EntidadNoEncontrada;
import estaciones.repositorio.RepositorioBicicletas;
import estaciones.repositorio.RepositorioEstaciones;
import estaciones.repositorio.RepositorioException;
import estaciones.rest.EstacionResumen;
import estaciones.rest.dto.BicicletaDTO;
import estaciones.rest.dto.EstacionDTO;
import eventos.bus.PublicadorEventos;

@Service
public class ServicioEstaciones implements IServicioEstaciones {

    private RepositorioEstaciones repoEstaciones;
    private RepositorioBicicletas repoBicicletas;
    private PublicadorEventos sender;

    @Autowired
    public ServicioEstaciones(RepositorioEstaciones repoEstaciones, RepositorioBicicletas repoBicicletas,
                              PublicadorEventos sender) {
        this.repoEstaciones = repoEstaciones;
        this.repoBicicletas = repoBicicletas;
        this.sender = sender;
    }

    // FUNCIONALIDAD ROL GESTOR

    @Override
    public String altaEstacion(String nombre, int numPuestos, String direccion, double latitud, double longitud) {

        if (nombre == null || nombre.isEmpty())
            throw new IllegalArgumentException("nombre: no debe ser nulo ni vacio");

        if (numPuestos < 1)
            throw new IllegalArgumentException("numero de puestos: debe ser mayor que 0");

        if (direccion == null || direccion.isEmpty())
            throw new IllegalArgumentException("direccion: no debe ser nulo ni vacio");

        Estacion nuevaEstacion = new Estacion(nombre, numPuestos, direccion, latitud, longitud, LocalDateTime.now());
        String id = repoEstaciones.save(nuevaEstacion).getId();
        return id;

    }

    @Override
    public String altaBicicleta(String modelo, String idEstacion) throws RepositorioException, EntidadNoEncontrada {
        if (modelo == null || modelo.isEmpty())
            throw new IllegalArgumentException("modelo: no debe ser nulo ni vacio");

        if (idEstacion == null || idEstacion.isEmpty())
            throw new IllegalArgumentException("idEstacion: no debe ser nulo ni vacio");

        Estacion estacion = repoEstaciones.findById(idEstacion)
                .orElseThrow(() -> new EntidadNoEncontrada("No existe estacion con id: " + idEstacion));

        if (estacion.estacionamientoDisponible()) {
            Bicicleta bicicleta = new Bicicleta();
            bicicleta.setModelo(modelo);
            bicicleta.setFechaAlta(LocalDateTime.now());
            bicicleta.setDisponible(true);
            repoBicicletas.save(bicicleta);
            estacion.agregarBicicleta(bicicleta);
            repoEstaciones.save(estacion);

            return bicicleta.getId();
        } else {
            throw new RepositorioException("No hay puestos libres en la estación");
        }
    }

    @Override
    public void bajaBicicleta(String idBicicleta, LocalDateTime fecha, String motivo)
            throws RepositorioException, EntidadNoEncontrada {
        if (idBicicleta == null || idBicicleta.isEmpty())
            throw new IllegalArgumentException("idBicicleta: no debe ser nulo ni vacio");

        if (motivo == null || motivo.isEmpty())
            throw new IllegalArgumentException("motivo: no debe ser nulo ni vacio");

        Optional<Bicicleta> bicicletaOpt = repoBicicletas.findById(idBicicleta);
        if (bicicletaOpt.isPresent()) {
            Bicicleta bicicleta = bicicletaOpt.get();
            bicicleta.setDisponible(false);
            bicicleta.setFechaBaja(fecha);
            bicicleta.setMotivoBaja(motivo);
            Estacion estacion = repoEstaciones.findByBicicletasId(bicicleta.getId());
            if (estacion != null) {
                estacion.eliminarBicicleta(bicicleta);
                repoEstaciones.save(estacion);
            }
            repoBicicletas.save(bicicleta);

            System.out.println("BICICLETA DADA DE BAJA");

            EventoBicicletaDesactivada evento = new EventoBicicletaDesactivada(bicicleta.getId());
            sender.emitirEvento(evento);

        } else {
            throw new EntidadNoEncontrada("Bicicleta no encontrada con ID: " + idBicicleta);
        }

    }

    @Override
    public void bajaBicicleta(String idBicicleta, String motivo) throws RepositorioException, EntidadNoEncontrada {
        bajaBicicleta(idBicicleta, LocalDateTime.now(), motivo);
    }

    @Override
    public Page<BicicletaDTO> getBicicletasEstacionPaginado(String idEstacion, Pageable pageable)
            throws EntidadNoEncontrada {
        Estacion estacion = repoEstaciones.findById(idEstacion)
                .orElseThrow(() -> new EntidadNoEncontrada("Estación no encontrada"));

        List<Bicicleta> bicicletas = estacion.getBicicletas();
        List<BicicletaDTO> bicicletasDto = bicicletas.stream().map(BicicletaDTO::fromEntity)
                .collect(Collectors.toList());

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), bicicletasDto.size());
        List<BicicletaDTO> subList = bicicletasDto.subList(start, end);

        return new PageImpl<>(subList, pageable, bicicletasDto.size());
    }

    // FUNCIONALIDAD ROL USUARIO

    @Override
    public Page<EstacionDTO> listarEstaciones(Pageable pageable) {
        return this.repoEstaciones.findAll(pageable).map(EstacionDTO::fromEntity);
    }

    @Override
    public EstacionResumen obtenerEstacion(String id) throws EntidadNoEncontrada {
        if (id == null || id.isEmpty())
            throw new IllegalArgumentException("id: no debe ser nulo ni vacio");

        Optional<Estacion> resultado = repoEstaciones.findById(id);
        if (!resultado.isPresent())
            throw new EntidadNoEncontrada("no existe estacion: " + id);
        else
            return EstacionResumen.fromEntity(resultado.get());
    }

    @Override
    public Page<BicicletaDTO> listarBicicletasDisponiblesEstacion(String idEstacion, Pageable pageable)
            throws RepositorioException, EntidadNoEncontrada {

        if (idEstacion == null || idEstacion.isEmpty())
            throw new IllegalArgumentException("idEstacion: no debe ser nulo ni vacio");

        Estacion estacion = repoEstaciones.findById(idEstacion)
                .orElseThrow(() -> new EntidadNoEncontrada("Estación no encontrada"));

        List<Bicicleta> bicicletasDisponibles = estacion.getBicicletasDisponibles();
        List<BicicletaDTO> bicicletasDto = bicicletasDisponibles.stream().map(BicicletaDTO::fromEntity)
                .collect(Collectors.toList());

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), bicicletasDto.size());
        List<BicicletaDTO> subList = bicicletasDto.subList(start, end);

        return new PageImpl<>(subList, pageable, bicicletasDto.size());
    }

    @Override
    public void estacionarBicicleta(String idEstacion, String idBicicleta)
            throws RepositorioException, EntidadNoEncontrada {

        if (idBicicleta == null || idBicicleta.isEmpty())
            throw new IllegalArgumentException("idBicicleta: no debe ser nulo ni vacio");

        if (idEstacion == null || idEstacion.isEmpty())
            throw new IllegalArgumentException("idEstacion: no debe ser nulo ni vacio");

        Estacion estacion = repoEstaciones.findById(idEstacion)
                .orElseThrow(() -> new EntidadNoEncontrada("No existe estacion con id: " + idEstacion));

        if (estacion.estacionamientoDisponible()) {
            Optional<Bicicleta> bicicletaOpt = repoBicicletas.findById(idBicicleta);
            if (bicicletaOpt.isPresent()) {
                Bicicleta bicicleta = bicicletaOpt.get();
                bicicleta.setFechaBaja(null);
                bicicleta.setMotivoBaja(null);
                repoBicicletas.save(bicicleta);
                estacion.agregarBicicleta(bicicleta);
                repoEstaciones.save(estacion);

            } else {
                throw new EntidadNoEncontrada("Bicicleta no encontrada con ID: " + idBicicleta);
            }
        } else {
            throw new RepositorioException("No hay puestos libres en la estación");
        }
    }

    @Override
    public void bicicletaDisponible(String idBicicleta) throws EntidadNoEncontrada {
        if (idBicicleta == null || idBicicleta.isEmpty())
            throw new IllegalArgumentException("idBicicleta: no debe ser nulo ni vacio");

        Bicicleta bicicleta = repoBicicletas.findById(idBicicleta)
                .orElseThrow(() ->new EntidadNoEncontrada("Bicicleta no encontrada con ID: " + idBicicleta));

        bicicleta.setDisponible(true);
        repoBicicletas.save(bicicleta);

    }

}
