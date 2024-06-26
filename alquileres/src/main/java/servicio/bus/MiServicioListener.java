package servicio.bus;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import alquileres.servicio.IServicioAlquiler;
import eventos.ServicioEventosEstaciones;
import servicio.FactoriaServicios;

public class MiServicioListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
    	ServicioEventosEstaciones servicio = new ServicioEventosEstaciones();
    	
    	servicio.iniciarConsumoEventos();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("Deteniendo servicio...");
        detenerServicio();
    }

    private void detenerServicio() {

    }
}

