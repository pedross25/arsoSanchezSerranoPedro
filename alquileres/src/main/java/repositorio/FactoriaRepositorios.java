package repositorio;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import servicio.FactoriaServicios;

/**
 * Factoria que encapsula la implementación del repositorio.
 *
 */
public class FactoriaRepositorios {

	private static final String PROPERTIES = "repositorios.properties";

	private static Map<Class<?>, Object> repositorios = new HashMap<>();
	
	@SuppressWarnings("unchecked")
	public static <R> R getRepositorio(Class<?> entidad) {
		try {
			if (repositorios.containsKey(entidad)) {
				return (R) repositorios.get(entidad);
			}
			else {
				Properties propiedades = new Properties();
				propiedades
						.load(FactoriaServicios.class.getClassLoader().getResourceAsStream(PROPERTIES));

				String clase = propiedades.getProperty(entidad.getName());
				return (R) Class.forName(clase).getConstructor().newInstance();
			}
		} catch (Exception e) {
			throw new RuntimeException("No se ha podido obtener el repositorio para la entidad: " + entidad.getName());
		}
	}
}
