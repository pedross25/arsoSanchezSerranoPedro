package programa;

import alquileres.modelo.Usuario;
import alquileres.servicio.IServicioAlquiler;
import alquileres.servicio.ServicioAlquileresException;
import repositorio.EntidadNoEncontrada;
import repositorio.OperacionNoPermitida;
import repositorio.RepositorioException;
import servicio.FactoriaServicios;

public class ProgramaPrueba {
	
	public static void main(String[] args) {
		
        IServicioAlquiler servicioAlquiler = FactoriaServicios.getServicio(IServicioAlquiler.class);
        
        String idUsuario = "user123";
        String idBicicleta = "666d6ed488826d7700aa3104";
        
        
//        try {
//			servicioAlquiler.alquilar(idUsuario, idBicicleta);
//		} catch (RepositorioException | EntidadNoEncontrada e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
        
        try {
			servicioAlquiler.alquilar(idUsuario, idBicicleta);
		} catch (RepositorioException | EntidadNoEncontrada e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OperacionNoPermitida e) {
            throw new RuntimeException(e);
        }

        try {
			servicioAlquiler.dejarBicicleta(idUsuario, idBicicleta);
		} catch (RepositorioException | EntidadNoEncontrada | ServicioAlquileresException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       

//        String idUsuario = "user123";
//        String idBicicleta = "bike123";
//
//        try {
//        	Usuario usuario = new Usuario();
//			usuario.setId(idUsuario);
//			//repositorio.add(usuario);
//        	
//            // Intentar reservar una bicicleta
//            servicioAlquiler.reservar(idUsuario, idBicicleta);
//            //servicioAlquiler.reservar(idUsuario, idBicicleta);
//            System.out.println("Reserva creada para el usuario " + idUsuario);
//            
//            
//            
//
//            // Confirmar la reserva
//            servicioAlquiler.confirmarReserva(idUsuario);
//            System.out.println("Reserva confirmada para el usuario " + idUsuario);
//
//            // Intentar alquilar una bicicleta
//            servicioAlquiler.alquilar(idUsuario, idBicicleta);
//            System.out.println("Bicicleta alquilada para el usuario " + idUsuario);
//
//            // Dejar la bicicleta en una estación
//            String idEstacion = "station123";
//            try {
//				servicioAlquiler.dejarBicicleta(idUsuario, idEstacion);
//			} catch (ServicioAlquileresException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//            System.out.println("Bicicleta dejada en la estación " + idEstacion);
//
//        } catch (RepositorioException | EntidadNoEncontrada e) {
//            e.printStackTrace();
//        }
    }

}
