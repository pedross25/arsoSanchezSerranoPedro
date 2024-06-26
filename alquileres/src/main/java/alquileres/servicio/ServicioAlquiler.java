package alquileres.servicio;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

import alquileres.persistencia.dto.Mapper;
import alquileres.persistencia.jpa.RepositorioUsuarios;

import alquileres.modelo.Alquiler;
import alquileres.modelo.Reserva;
import alquileres.modelo.Usuario;
import repositorio.*;
import servicio.FactoriaServicios;
import eventos.EventoBicicletaAlquilada;
import eventos.EventoBicicletaAlquilerConcluido;
import eventos.servicio.EventBusService;
import eventos.servicio.EventConsumer;
import eventos.servicio.IEventBusService;

public class ServicioAlquiler implements IServicioAlquiler {

    private Repositorio<Usuario, String> repositorio = FactoriaRepositorios.getRepositorio(Usuario.class);
    private IServicioEstaciones servicioEstaciones = FactoriaServicios.getServicio(IServicioEstaciones.class);
    private IEventBusService busService = FactoriaServicios.getServicio(IEventBusService.class);

    private static String routingKeyAlquilada = "citybike.alquileres.bicicleta-alquilada";
    private static String routingKeyConcluido = "citybike.alquileres.bicicleta-alquiler-concluido";

    @Override
    public void reservar(String idUsuario, String idBicicleta) throws RepositorioException, EntidadNoEncontrada, OperacionNoPermitida {

        if (idUsuario == null || idUsuario.isEmpty())
            throw new IllegalArgumentException("El identificador del usuario no debe ser nulo o vacío");

        if (idBicicleta == null || idBicicleta.isEmpty()) {
            throw new IllegalArgumentException("El identificador de la bicicleta no debe ser nulo o vacío");
        }
        Usuario usuario;
        try {
            usuario = repositorio.getById(idUsuario);
            System.out.println("Usuario recuperado");
        } catch (EntidadNoEncontrada e) {
            System.out.println("Creando usuario");
            usuario = new Usuario();
            usuario.setId(idUsuario);
            repositorio.add(usuario);
        }

        if (usuario.getReservaActiva() != null) {
            throw new OperacionNoPermitida("El usuario tiene una reserva activa");
        }

        if (usuario.getAlquilerActivo() != null) {
            throw new OperacionNoPermitida("El usuario tiene un alquiler activo");
        }

        if (usuario.isBloqueado()) {
            throw new OperacionNoPermitida("El usuario está bloqueado");
        }

        Reserva reserva = new Reserva();
        reserva.setIdBicicleta(idBicicleta);
        LocalDateTime creacion = LocalDateTime.now();
        reserva.setCreada(creacion);
        reserva.setCaducidad(creacion.plusMinutes(30));

        List<Reserva> reservas = usuario.getReservas();
        reservas.add(reserva);
        usuario.setReservas(reservas);
        repositorio.update(usuario);
    }

    @Override
    public void confirmarReserva(String idUsuario) throws RepositorioException, EntidadNoEncontrada, OperacionNoPermitida {
        if (idUsuario == null || idUsuario.isEmpty())
            throw new IllegalArgumentException("El identificador del usuario no debe ser nulo o vacío");

        Usuario usuario = repositorio.getById(idUsuario);
        Reserva reserva = usuario.getReservaActiva();

        if (reserva == null) {
            throw new OperacionNoPermitida("El usuario no tiene una reserva activa");
        }

        Alquiler alquiler = new Alquiler();
        alquiler.setId(reserva.getIdBicicleta());
        alquiler.setInicio(LocalDateTime.now());

        List<Reserva> nuevasReservas = usuario.getReservas();
        nuevasReservas.remove(reserva);
        usuario.setReservas(nuevasReservas);

        List<Alquiler> alquileres = usuario.getAlquileres();
        alquileres.add(alquiler);
        usuario.setAlquileres(alquileres);

        repositorio.update(usuario);
    }

    @Override
    public void alquilar(String idUsuario, String idBicicleta) throws RepositorioException, EntidadNoEncontrada, OperacionNoPermitida {
        if (idUsuario == null || idUsuario.isEmpty())
            throw new IllegalArgumentException("El identificador del usuario no debe ser nulo o vacío");

        if (idBicicleta == null || idBicicleta.isEmpty()) {
            throw new IllegalArgumentException("El identificador de la bicicleta no debe ser nulo o vacío");
        }

        Usuario usuario;
        try {
            usuario = repositorio.getById(idUsuario);
        } catch (EntidadNoEncontrada e) {
            usuario = new Usuario();
            usuario.setId(idUsuario);
            repositorio.add(usuario);
        }

        if (usuario.getReservaActiva() != null) {
            throw new OperacionNoPermitida("El usuario tiene una reserva activa");
        }

        if (usuario.getAlquilerActivo() != null) {
            throw new OperacionNoPermitida("El usuario tiene un alquiler activo");
        }

        if (usuario.isBloqueado()) {
            throw new OperacionNoPermitida("El usuario está bloqueado");
        }

        if (usuario.superaTiempo()) {
            throw new OperacionNoPermitida("El usuario ha superado el tiempo de uso");
        }

        Alquiler alquiler = new Alquiler();
        alquiler.setId(idBicicleta);
        LocalDateTime inicio = LocalDateTime.now();
        alquiler.setInicio(inicio);

        List<Alquiler> alquileres = usuario.getAlquileres();
        alquileres.add(alquiler);
        usuario.setAlquileres(alquileres);

        repositorio.update(usuario);

        EventoBicicletaAlquilada evento = new EventoBicicletaAlquilada(idBicicleta, inicio.toString());

        try {
            busService.notificarEvento(routingKeyAlquilada, evento);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public UsuarioResumen hitorialUsuario(String idUsuario) throws RepositorioException, EntidadNoEncontrada {
        if (idUsuario == null || idUsuario.isEmpty())
            throw new IllegalArgumentException("El identificador del usuario no debe ser nulo o vacío");

        Usuario usuario = repositorio.getById(idUsuario);

        UsuarioResumen resumen = new UsuarioResumen();
        resumen.setIdUsuario(idUsuario);
        resumen.setAlquileres(usuario.getAlquileres().stream()
                .map(Mapper::toAlquilerDTO)
                .collect(Collectors.toList()));
        resumen.setReservas(usuario.getReservas().stream()
                .map(Mapper::toReservaDTO)
                .collect(Collectors.toList()));
        resumen.setBloqueado(usuario.isBloqueado());
        resumen.setTiempoUsoHoy(usuario.getTiempoUsoHoy());
        resumen.setTiempoUsoSemana(usuario.getTiempoUsoSemana());

        return resumen;
    }

    @Override
    public void dejarBicicleta(String idUsuario, String idEstacion)
            throws RepositorioException, EntidadNoEncontrada, ServicioAlquileresException {
        if (idUsuario == null || idUsuario.isEmpty())
            throw new IllegalArgumentException("El identificador del usuario no debe ser nulo o vacío");

        if (idEstacion == null || idEstacion.isEmpty()) {
            throw new IllegalArgumentException("El identificador de la estación no debe ser nulo o vacío");
        }

        Usuario usuario = repositorio.getById(idUsuario);
        Alquiler alquiler = usuario.getAlquilerActivo();

        if (alquiler == null) {
            throw new RepositorioException("El usuario no tiene un alquiler activo");
        }

        if (!servicioEstaciones.isHuecoDisponible(idEstacion)) {
            throw new RepositorioException("La estación no tiene un hueco disponible");
        }

        if (!servicioEstaciones.situarBicicleta(idEstacion, alquiler.getId())) {
            throw new RepositorioException("No se ha poddido situar la bicicleta en la estación");
        }

        alquiler.setFin(LocalDateTime.now());
        repositorio.update(usuario);

        EventoBicicletaAlquilerConcluido evento = new EventoBicicletaAlquilerConcluido(idEstacion, alquiler.getId());
        try {
            busService.notificarEvento(routingKeyConcluido, evento);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void liberarBloqueo(String idUsuario) throws RepositorioException, EntidadNoEncontrada {
        if (idUsuario == null || idUsuario.isEmpty())
            throw new IllegalArgumentException("El identificador del usuario no debe ser nulo o vacío");

        Usuario usuario = repositorio.getById(idUsuario);
        List<Reserva> reservasNoCaducadas = usuario.getReservas().stream().filter(reserva -> !reserva.isCaducada())
                .collect(Collectors.toList());

        usuario.setReservas(reservasNoCaducadas);
        repositorio.update(usuario);
    }

    @Override
    public void eliminarReservaActiva(String idBicicleta) throws RepositorioException, EntidadNoEncontrada {
        if (idBicicleta == null || idBicicleta.isEmpty())
            throw new IllegalArgumentException("El identificador del usuario no debe ser nulo o vacío");

        List<Usuario> usuarios = repositorio.getAll();

        for (Usuario usuario : usuarios) {
            Reserva reservaActiva = usuario.getReservaActiva();
            if (reservaActiva != null && reservaActiva.getIdBicicleta().equals(idBicicleta)) {
                usuario.getReservas().remove(reservaActiva);
                repositorio.update(usuario);
            }
        }

    }

}
