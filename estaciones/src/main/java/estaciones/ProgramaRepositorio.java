package estaciones;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import estaciones.modelo.Bicicleta;
import estaciones.modelo.Estacion;
import estaciones.repositorio.RepositorioBicicletas;
import estaciones.repositorio.RepositorioEstaciones;

public class ProgramaRepositorio {

	public static void main(String[] args) {
		ConfigurableApplicationContext contexto = SpringApplication.run(EstacionesApplication.class, args);

		RepositorioEstaciones repositorio = contexto.getBean(RepositorioEstaciones.class);
		RepositorioBicicletas repoBicicletas = contexto.getBean(RepositorioBicicletas.class);
		
		//crearEstaciones(repositorio);
		//pruebaRepositorio(repositorio);
		crearBicicletas(repoBicicletas, repositorio);

		System.out.println(repositorio.count());

		contexto.close();
	}

	private static void crearEstaciones(RepositorioEstaciones repositorio) {
		// Crear 5 estaciones
		Estacion estacion1 = new Estacion("Estacion Central", 50, "Calle Central, 123", 40.7128, -74.0060,
				LocalDateTime.now());
		Estacion estacion2 = new Estacion("Estacion Norte", 30, "Avenida Norte, 456", 40.7890, -74.0000,
				LocalDateTime.now());
		Estacion estacion3 = new Estacion("Estacion Sur", 20, "Boulevard Sur, 789", 40.7000, -74.0100,
				LocalDateTime.now());
		Estacion estacion4 = new Estacion("Estacion Este", 40, "Camino Este, 321", 40.7230, -74.0150,
				LocalDateTime.now());
		Estacion estacion5 = new Estacion("Estacion Oeste", 25, "Plaza Oeste, 654", 40.7300, -74.0200,
				LocalDateTime.now());

		// Guardar estaciones en la base de datos
		repositorio.save(estacion1);
		repositorio.save(estacion2);
		repositorio.save(estacion3);
		repositorio.save(estacion4);
		repositorio.save(estacion5);

		// Listar todas las estaciones
		List<Estacion> todasLasEstaciones = (List<Estacion>) repositorio.findAll();
		System.out.println("Todas las estaciones:");
		todasLasEstaciones.forEach(System.out::println);
	}
	
	private static void crearBicicletas(RepositorioBicicletas repoBicicletas, RepositorioEstaciones repoEstaciones) {
		
        Optional<Estacion> estacionOpt = repoEstaciones.findById("6676a4177a242f55986e3021");

        if (estacionOpt.isPresent()) {
        	Estacion estacion = estacionOpt.get();
        	Bicicleta bicicleta1 = new Bicicleta("Modelo A2", LocalDateTime.now(), true);
        	Bicicleta bicicleta2 = new Bicicleta("Modelo B", LocalDateTime.now(), true);
        	Bicicleta bicicleta3 = new Bicicleta("Modelo C", LocalDateTime.now(), true);
        	Bicicleta bicicleta4 = new Bicicleta("Modelo D", LocalDateTime.now(), true);
        	estacion.agregarBicicleta(bicicleta1);
        	estacion.agregarBicicleta(bicicleta2);
        	estacion.agregarBicicleta(bicicleta3);
        	estacion.agregarBicicleta(bicicleta4);
        	repoBicicletas.save(bicicleta1);
        	repoBicicletas.save(bicicleta2);
        	repoBicicletas.save(bicicleta3);
        	repoBicicletas.save(bicicleta4);
        	repoEstaciones.save(estacion);
        	
        }  

	}

	private static void pruebaRepositorio(RepositorioEstaciones repositorio) {

		// Crear una nueva estación
		Estacion nuevaEstacion = new Estacion("Estacion Central", 50, "Calle Central, 123", 40.7128, -74.0060,
				LocalDateTime.now());
		Estacion estacionGuardada = repositorio.save(nuevaEstacion);
		System.out.println("Estación guardada: " + estacionGuardada);

		// Buscar una estación por ID
		Optional<Estacion> estacionEncontrada = repositorio.findById(estacionGuardada.getId());
		if (estacionEncontrada.isPresent()) {
			System.out.println("Estación encontrada: " + estacionEncontrada.get());
		} else {
			System.out.println("Estación no encontrada");
		}

		// Listar todas las estaciones
		List<Estacion> todasLasEstaciones = (List<Estacion>) repositorio.findAll();
		System.out.println("Todas las estaciones:");
		todasLasEstaciones.forEach(System.out::println);

		// Actualizar una estación
		if (estacionEncontrada.isPresent()) {
			Estacion estacionParaActualizar = estacionEncontrada.get();
			estacionParaActualizar.setNombre("Estacion Actualizada");
			repositorio.save(estacionParaActualizar);
			System.out.println("Estación actualizada: " + estacionParaActualizar);
		}

		// Eliminar una estación
		if (estacionEncontrada.isPresent()) {
			repositorio.delete(estacionEncontrada.get());
			System.out.println("Estación eliminada: " + estacionEncontrada.get());
		}

		// Verificar que la estación ha sido eliminada
		todasLasEstaciones = (List<Estacion>) repositorio.findAll();
		System.out.println("Estaciones restantes después de eliminar:");
		todasLasEstaciones.forEach(System.out::println);

	}

}
