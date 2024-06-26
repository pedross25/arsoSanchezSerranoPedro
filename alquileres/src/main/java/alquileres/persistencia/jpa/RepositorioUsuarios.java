package alquileres.persistencia.jpa;

import alquileres.modelo.Usuario;
import alquileres.modelo.Alquiler;
import alquileres.modelo.Reserva;
import alquileres.persistencia.RepositorioJpaUsuarios;
import alquileres.persistencia.jpa.modelo.AlquilerEntidad;
import alquileres.persistencia.jpa.modelo.ReservaEntidad;
import repositorio.EntidadNoEncontrada;
import repositorio.Repositorio;
import repositorio.RepositorioException;
import repositorio.RepositorioString;

import java.util.List;


import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;
import java.util.stream.Collectors;

public class RepositorioUsuarios implements RepositorioString<Usuario> {

    private IRepositorioJpaUsuarios<AlquilerEntidad> alquilerRepositorio = new RepositorioJpaUsuarios<>(AlquilerEntidad.class);
    private IRepositorioJpaUsuarios<ReservaEntidad> reservaRepositorio = new RepositorioJpaUsuarios<>(ReservaEntidad.class);

    @Override
    public String add(Usuario usuario) throws RepositorioException {
//        EntityManager em = EntityManagerHelper.getEntityManager();
//        EntityTransaction transaction = em.getTransaction();
//        try {
//            transaction.begin();

            // Guardar los alquileres del usuario
            for (Alquiler alquiler : usuario.getAlquileres()) {
                AlquilerEntidad alquilerEntidad = convertirAEntidad(alquiler, usuario.getId());
                alquilerRepositorio.add(alquilerEntidad); // Asumiendo que esto usa el mismo EntityManager
            }

            // Guardar las reservas del usuario
            for (Reserva reserva : usuario.getReservas()) {
                ReservaEntidad reservaEntidad = convertirAEntidad(reserva, usuario.getId());
                reservaRepositorio.add(reservaEntidad); // Asumiendo que esto usa el mismo EntityManager
            }

            return "1234";

//            transaction.commit();
//        } catch (Exception e) {
//            if (transaction.isActive()) {
//                transaction.rollback();
//            }
//            throw new RepositorioException("Error al añadir el usuario " + usuario.getId(), e);
//        }
//        return usuario.getId();
    }



    @Override
    public void update(Usuario usuario) throws RepositorioException, EntidadNoEncontrada {
        List<AlquilerEntidad> alquileresEntidad = alquilerRepositorio.findByUsuarioId(usuario.getId());
        List<Alquiler> alquileres = usuario.getAlquileres();

        for (Alquiler alquiler : alquileres) {
            AlquilerEntidad entidad = alquileresEntidad.stream()
                    .filter(ae -> ae.getInicio().equals(alquiler.getInicio()) )
                    .findFirst()
                    .orElse(new AlquilerEntidad());

            entidad.setFin(alquiler.getFin());
            entidad.setIdBicicleta(alquiler.getId());
            entidad.setInicio(alquiler.getInicio());
            entidad.setUsuarioId(usuario.getId());

            alquilerRepositorio.update(entidad);
        }

        for (AlquilerEntidad alquiler : alquileresEntidad) {
            if (alquileres.stream().noneMatch(a -> a.getInicio().equals(alquiler.getInicio()))) {
                EntityManager em = EntityManagerHelper.getEntityManager();
                em.merge(alquiler);
                alquilerRepositorio.delete(alquiler);
            }
        }

        List<ReservaEntidad> reservasEntidad = reservaRepositorio.findByUsuarioId(usuario.getId());
        List<Reserva> reservas = usuario.getReservas();

        for (ReservaEntidad reserva : reservasEntidad) {
            if (reservas.stream().noneMatch(r -> r.getCreada().equals(reserva.getCreada()))) {
                EntityManager em = EntityManagerHelper.getEntityManager();
                //m.merge(reserva);
                reservaRepositorio.delete(reserva);
            }
        }

        for (Reserva reserva : reservas) {
            ReservaEntidad entidad = reservasEntidad.stream()
                    .filter(ae -> ae.getCreada().equals(reserva.getCreada()) )
                    .findFirst()
                    .orElse(new ReservaEntidad());

            entidad.setIdBicicleta(reserva.getIdBicicleta());
            entidad.setCaducidad(reserva.getCaducidad());
            entidad.setCreada(reserva.getCreada());
            entidad.setUsuarioId(usuario.getId());

            reservaRepositorio.update(entidad);
        }




//            em.getTransaction().commit();
//        } catch (RepositorioException | EntidadNoEncontrada e) {
//            em.getTransaction().rollback();
//            throw e;
//        } finally {
//            EntityManagerHelper.closeEntityManager();
//        }
    }

    @Override
    public void delete(Usuario usuario) throws RepositorioException, EntidadNoEncontrada {
//        EntityManager em = EntityManagerHelper.getEntityManager();
//        try {
//            em.getTransaction().begin();

            // Eliminar los alquileres del usuario

            reservaRepositorio.deleteByUsuarioId(usuario.getId());
            alquilerRepositorio.deleteByUsuarioId(usuario.getId());
//            for (Alquiler alquiler : usuario.getAlquileres()) {
//                AlquilerEntidad alquilerEntidad = convertirAEntidad(alquiler, usuario.getId());
//                alquilerRepositorio.delete(alquilerEntidad);
//            }
//
//            // Eliminar las reservas del usuario
//            for (Reserva reserva : usuario.getReservas()) {
//                ReservaEntidad reservaEntidad = convertirAEntidad(reserva, usuario.getId());
//                reservaRepositorio.delete(reservaEntidad);
//            }

//            em.getTransaction().commit();
//        } catch (RepositorioException | EntidadNoEncontrada e) {
//            em.getTransaction().rollback();
//            throw e;
//        } finally {
//            EntityManagerHelper.closeEntityManager();
//        }
    }

    @Override
    public Usuario getById(String id) throws RepositorioException, EntidadNoEncontrada {
        EntityManager em = EntityManagerHelper.getEntityManager();
        try {
            Usuario usuario = new Usuario();
            usuario.setId(id);

            // Obtener los alquileres del usuario
            List<AlquilerEntidad> alquileresEntidad = em.createQuery(
                            "SELECT a FROM AlquilerEntidad a WHERE a.usuarioId = :usuarioId", AlquilerEntidad.class)
                    .setParameter("usuarioId", id)
                    .getResultList();
            List<Alquiler> alquileres = alquileresEntidad.stream()
                    .map(this::convertirADominio)
                    .collect(Collectors.toList());
            usuario.setAlquileres(alquileres);

            // Obtener las reservas del usuario
            List<ReservaEntidad> reservasEntidad = em.createQuery(
                            "SELECT r FROM ReservaEntidad r WHERE r.usuarioId = :usuarioId", ReservaEntidad.class)
                    .setParameter("usuarioId", id)
                    .getResultList();
            List<Reserva> reservas = reservasEntidad.stream()
                    .map(this::convertirADominio)
                    .collect(Collectors.toList());
            usuario.setReservas(reservas);

            return usuario;
        } finally {
            EntityManagerHelper.closeEntityManager();
        }
    }

    @Override
    public List<Usuario> getAll() throws RepositorioException {
        EntityManager em = EntityManagerHelper.getEntityManager();
        try {
            // Obtener todos los IDs de los usuarios
            List<String> usuarioIds = em.createQuery(
                            "SELECT DISTINCT a.usuarioId FROM AlquilerEntidad a UNION SELECT DISTINCT r.usuarioId FROM ReservaEntidad r", String.class)
                    .getResultList();

            // Obtener todos los usuarios
            return usuarioIds.stream()
                    .map(id -> {
                        try {
                            return getById(id);
                        } catch (RepositorioException | EntidadNoEncontrada e) {
                            e.printStackTrace();
                            return null;
                        }
                    })
                    .collect(Collectors.toList());
        } finally {
            EntityManagerHelper.closeEntityManager();
        }
    }

    @Override
    public List<String> getIds() throws RepositorioException {
        EntityManager em = EntityManagerHelper.getEntityManager();
        try {
            return em.createQuery(
                            "SELECT DISTINCT a.usuarioId FROM AlquilerEntidad a UNION SELECT DISTINCT r.usuarioId FROM ReservaEntidad r", String.class)
                    .getResultList();
        } finally {
            EntityManagerHelper.closeEntityManager();
        }
    }

    private AlquilerEntidad convertirAEntidad(Alquiler alquiler, String usuarioId) {
        AlquilerEntidad alquilerEntidad = new AlquilerEntidad();
        alquilerEntidad.setIdBicicleta(alquiler.getId());
        alquilerEntidad.setInicio(alquiler.getInicio());
        alquilerEntidad.setFin(alquiler.getFin());
        alquilerEntidad.setUsuarioId(usuarioId);
        return alquilerEntidad;
    }

    private ReservaEntidad convertirAEntidad(Reserva reserva, String usuarioId) {
        ReservaEntidad reservaEntidad = new ReservaEntidad();
        reservaEntidad.setCreada(reserva.getCreada());
        reservaEntidad.setCaducidad(reserva.getCaducidad());
        reservaEntidad.setUsuarioId(usuarioId);
        reservaEntidad.setIdBicicleta(reserva.getIdBicicleta());
        return reservaEntidad;
    }

    private Alquiler convertirADominio(AlquilerEntidad alquilerEntidad) {
        Alquiler alquiler = new Alquiler();
        alquiler.setId(alquilerEntidad.getIdBicicleta());
        alquiler.setInicio(alquilerEntidad.getInicio());
        alquiler.setFin(alquilerEntidad.getFin());
        return alquiler;
    }

    private Reserva convertirADominio(ReservaEntidad reservaEntidad) {
        Reserva reserva = new Reserva();
        reserva.setIdBicicleta(reservaEntidad.getIdBicicleta());
        reserva.setCreada(reservaEntidad.getCreada());
        reserva.setCaducidad(reservaEntidad.getCaducidad());
        return reserva;
    }
}