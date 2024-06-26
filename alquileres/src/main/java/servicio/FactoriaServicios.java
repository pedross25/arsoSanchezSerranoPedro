package servicio;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class FactoriaServicios {
	
	private static final String PROPERTIES = "servicios.properties";
	
	private static Map<Class<?>, Object> servicios = new HashMap<>();
	
	@SuppressWarnings("unchecked")
	public static <R> R getServicio(Class<?> servicio) {
		try {
			if (servicios.containsKey(servicio)) {
				return (R) servicios.get(servicio);
			}
			else {
				Properties propiedades = new Properties();
				propiedades
						.load(FactoriaServicios.class.getClassLoader().getResourceAsStream(PROPERTIES));
				String clase = propiedades.getProperty(servicio.getName());
				return (R) Class.forName(clase).getConstructor().newInstance();
			}
		} catch (Exception e) {
			throw new RuntimeException("No se ha podido obtener el repositorio para la entidad: " + servicio.getName());
		}
	}
	
}