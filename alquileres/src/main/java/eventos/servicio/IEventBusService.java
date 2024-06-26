package eventos.servicio;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import eventos.Evento;

public interface IEventBusService {
	
	public void crearExchange();

	public void notificarEvento(String routingKey, Object eventData) throws Exception;
	
	//public void consumirEvento(EventConsumer consumer);
	
	public void consumirEvento(String queueName, String bindingKey, EventConsumer eventConsumer) throws IOException, TimeoutException;
}