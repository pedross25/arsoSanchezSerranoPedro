package eventos;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.fasterxml.jackson.databind.ObjectMapper;

import alquileres.servicio.IServicioAlquiler;
import eventos.servicio.EventBusService;
import eventos.servicio.EventConsumer;
import eventos.servicio.IEventBusService;
import servicio.FactoriaServicios;

public class ServicioEventosEstaciones {
	
	//private IEventBusService busService = FactoriaServicios.getServicio(IEventBusService.class);
	private IEventBusService busService = new EventBusService();
	private IServicioAlquiler servicio = FactoriaServicios.getServicio(IServicioAlquiler.class);
	
	//private static final String QUEUE_NAME = "citybike-alquileres";
	private static final String BINDING_KEY = "citybike.estaciones.*";
	
	private static final String EXCHANGE_NAME = "citybike";
	private static final String QUEUE_NAME = "citybike-alquileres";
	//private static final String BINDING_KEY = "#.alquileres";


	public void iniciarConsumoEventos() {

		busService.crearExchange();
		
		System.out.println("Consumiendo eventos...");
		
		ObjectMapper objectMapper = new ObjectMapper();

		try {
			busService.consumirEvento(QUEUE_NAME, BINDING_KEY, new EventConsumer() {
				@Override
				public void handleEvent(String message) {
					
					System.out.println("Evento recibido en ServicioAlquiler: " + message);
					
					System.out.println("Recibido mensaje: " + message);

	                try {
	                    // Deserializar el mensaje a un objeto EventoBicicletaDesactivada
	                    EventoBicicletaDesactivada evento = objectMapper.readValue(message, EventoBicicletaDesactivada.class);
	                    
	                    System.out.println(evento.getIdBicicleta());
	                    
	                    servicio.eliminarReservaActiva(evento.getIdBicicleta());
	                } catch (Exception e) {
	                    System.err.println("Error deserializando el mensaje: " + e.getMessage());
	                }
				}
			});
		} catch (IOException | TimeoutException e) {
			e.printStackTrace();
		}

	}

	
	
	

}
