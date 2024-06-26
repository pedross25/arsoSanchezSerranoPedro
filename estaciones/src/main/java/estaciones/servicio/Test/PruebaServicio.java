package estaciones.servicio.Test;

import estaciones.EstacionesApplication;
import estaciones.repositorio.EntidadNoEncontrada;
import estaciones.repositorio.RepositorioEstaciones;
import estaciones.repositorio.RepositorioException;
import estaciones.servicio.IServicioEstaciones;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = {"eventos.bus", "estaciones.servicio", "estaciones.security", "estaciones.rest", "eventos.config"})
public class PruebaServicio {

    public static void main(String[] args) {

        ConfigurableApplicationContext contexto = SpringApplication.run(EstacionesApplication.class, args);

        IServicioEstaciones servicio = contexto.getBean(IServicioEstaciones.class);

        try {
            servicio.bajaBicicleta("6676a46ac533c10398222490", "Prueba servicio y bus eventos");
        } catch (RepositorioException | EntidadNoEncontrada e) {
            throw new RuntimeException(e);
        }

    }
}
