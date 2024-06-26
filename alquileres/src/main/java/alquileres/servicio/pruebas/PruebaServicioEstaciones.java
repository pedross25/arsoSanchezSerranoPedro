package alquileres.servicio.pruebas;

import alquileres.servicio.IServicioEstaciones;
import alquileres.servicio.ServicioAlquileresException;
import servicio.FactoriaServicios;

public class PruebaServicioEstaciones {

	public static void main(String[] args) {

		IServicioEstaciones servicioEstaciones = FactoriaServicios.getServicio(IServicioEstaciones.class);

		try {
			boolean hueco = servicioEstaciones.isHuecoDisponible("666ad1e6d3b7c564e845586b");
			System.out.println(hueco);
		} catch (ServicioAlquileresException e) {
			System.out.println(e.toString());
		}

		try {
			servicioEstaciones.situarBicicleta("666ad1e6d3b7c564e845586b", "666d6e65ccd8cb4fcfb7ad7e");
		} catch (ServicioAlquileresException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}

	}

}
