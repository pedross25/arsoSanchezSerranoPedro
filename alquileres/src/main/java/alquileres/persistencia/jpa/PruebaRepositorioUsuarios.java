package alquileres.persistencia.jpa;

import alquileres.modelo.Alquiler;
import alquileres.modelo.Reserva;
import alquileres.modelo.Usuario;
import repositorio.EntidadNoEncontrada;
import repositorio.RepositorioException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PruebaRepositorioUsuarios {

    public static void main(String[] args) {
        // Crear un usuario de ejemplo
//        Usuario usuario = new Usuario();
//        usuario.setId("1");
//
//        // Crear alquiler de ejemplo
//        Alquiler alquiler = new Alquiler();
//        alquiler.setId("1");
//        alquiler.setId("001");
//        alquiler.setInicio(LocalDateTime.now());
//        alquiler.setFin(LocalDateTime.now().plusDays(5));
//
//        // Agregar alquiler al usuario
//        usuario.setAlquileres(new ArrayList<>());
//        usuario.getAlquileres().add(alquiler);
//
//        // Crear reserva de ejemplo
//        Reserva reserva = new Reserva();
//        reserva.setIdBicicleta("789432");
//        reserva.setCreada(LocalDateTime.now());
//        reserva.setCaducidad(LocalDateTime.now().plusDays(2));
//
//        // Agregar reserva al usuario
//        usuario.setReservas(new ArrayList<>());
//        usuario.getReservas().add(reserva);
//
//        // Instanciar el repositorio de usuarios
          RepositorioUsuarios repositorio = new RepositorioUsuarios();
//
//        // Agregar el usuario al repositorio
//        try {
//            System.out.println("Añadiendo usuario...");
//            repositorio.add(usuario);
//            System.out.println("Usuario añadido exitosamente.");
//        } catch (RepositorioException e) {
//            throw new RuntimeException("Error al añadir usuario", e);
//        }

       //  Obtener y mostrar usuario por ID
//        try {
//            System.out.println("\nObteniendo usuario por ID...");
//            Usuario usuarioEncontrado = repositorio.getById("1");
//            if (usuarioEncontrado != null) {
//                System.out.println("Usuario encontrado: " + usuarioEncontrado.getId());
//                System.out.println("Alquileres del usuario:");
//                usuarioEncontrado.getAlquileres().forEach(a ->
//                        System.out.println("- ID: " + a.getId() + ", ID Bicicleta: " + a.getId()));
//                System.out.println("Reservas del usuario:");
//                usuarioEncontrado.getReservas().forEach(r ->
//                        System.out.println("- ID Bicicleta: " + r.getIdBicicleta()));
//            } else {
//                System.out.println("Usuario no encontrado");
//            }
//        } catch (RepositorioException | EntidadNoEncontrada e) {
//            throw new RuntimeException("Error al obtener usuario por ID", e);
//        }

        // Actualizar el usuario
        try {
            Usuario usuario = repositorio.getById("1");
            System.out.println("\nActualizando usuario...");
            usuario.getAlquileres().forEach(a -> a.setId("002")); // Modificar ID de bicicleta de todos los alquileres
            repositorio.update(usuario);
            System.out.println("Usuario actualizado exitosamente.");
        } catch (RepositorioException | EntidadNoEncontrada e) {
            throw new RuntimeException("Error al actualizar usuario", e);
        }

        // Obtener y mostrar todos los usuarios
        try {
            System.out.println("\nObteniendo todos los usuarios...");
            List<Usuario> usuarios = repositorio.getAll();
            usuarios.forEach(u -> {
                System.out.println("Usuario ID: " + u.getId());
                System.out.println("Alquileres del usuario:");
                u.getAlquileres().forEach(a ->
                        System.out.println("- ID: " + a.getId() + ", ID Bicicleta: " + a.getId()));
                System.out.println("Reservas del usuario:");
                u.getReservas().forEach(r ->
                        System.out.println("- ID Bicicleta: " + r.getIdBicicleta()));
            });
        } catch (RepositorioException e) {
            throw new RuntimeException("Error al obtener todos los usuarios", e);
        }

        // Eliminar el usuario
        try {
            Usuario usuario = repositorio.getById("1");
            System.out.println("\nEliminando usuario...");
            repositorio.delete(usuario);
            System.out.println("Usuario eliminado exitosamente.");
        } catch (RepositorioException | EntidadNoEncontrada e) {
            throw new RuntimeException("Error al eliminar usuario", e);
        }
//
//        // Obtener y mostrar todos los IDs de usuarios
//        try {
//            System.out.println("\nObteniendo todos los IDs de usuarios...");
//            List<String> ids = repositorio.getIds();
//            ids.forEach(id -> System.out.println("ID de usuario: " + id));
//        } catch (RepositorioException e) {
//            throw new RuntimeException("Error al obtener los IDs de usuarios", e);
//        }
    }
}
