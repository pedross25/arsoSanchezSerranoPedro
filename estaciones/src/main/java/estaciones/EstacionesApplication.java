package estaciones;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(scanBasePackages={"eventos.bus.PublicadorEventos", "estaciones.security.JwtRequestFilter", "estaciones.rest.EstacionesController"})
@ComponentScan(basePackages = {"eventos.bus", "estaciones.servicio", "estaciones.security", "estaciones.rest", "eventos.config"})
public class EstacionesApplication {
	
	public static void main(String[] args) throws Exception {
		ConfigurableApplicationContext context = SpringApplication.run(EstacionesApplication.class, args);
	}
}