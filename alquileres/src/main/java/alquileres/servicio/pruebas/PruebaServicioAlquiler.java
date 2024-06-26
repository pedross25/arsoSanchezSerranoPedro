package alquileres.servicio.pruebas;

import alquileres.servicio.IServicioAlquiler;
import alquileres.servicio.ServicioAlquileresException;
import repositorio.EntidadNoEncontrada;
import repositorio.OperacionNoPermitida;
import repositorio.RepositorioException;
import servicio.FactoriaServicios;

public class PruebaServicioAlquiler {

	public static void main(String[] args) {
		
		IServicioAlquiler servicio = FactoriaServicios.getServicio(IServicioAlquiler.class);
		
		
		
		
//		try {
//			servicio.alquilar("user123", "6676a46ac533c10398222490");
//		} catch (RepositorioException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (EntidadNoEncontrada e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (OperacionNoPermitida e) {
//            throw new RuntimeException(e);
//        }

        try {
			servicio.dejarBicicleta("user1", "6676a4177a242f55986e3021");
		} catch (RepositorioException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (EntidadNoEncontrada e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ServicioAlquileresException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		

	}

}
