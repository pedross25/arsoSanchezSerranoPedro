package eventos.bus;

import com.fasterxml.jackson.databind.ObjectMapper;
import estaciones.servicio.IServicioEstaciones;
import eventos.config.RabbitMQConfig;
import eventos.model.EventoBicicletaAlquilada;
import eventos.model.EventoBicicletaAlquilerConcluido;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Component
public class EventListener {

    private final ObjectMapper objectMapper;

    private final Map<String, Class<?>> eventClassMap = new HashMap<>();

    private final IServicioEstaciones servicio;

    @Autowired
    public EventListener(ObjectMapper objectMapper, IServicioEstaciones servicio) {
        this.objectMapper = objectMapper;
        this.servicio = servicio;

        eventClassMap.put("citybike.alquileres.bicicleta-alquilada", EventoBicicletaAlquilada.class);
        eventClassMap.put("citybike.alquileres.bicicleta-alquiler-concluido", EventoBicicletaAlquilerConcluido.class);
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME_ESTACIONES)
    public void handleEvent(Message mensaje, @Header("amqp_receivedRoutingKey") String routingKey) {
        String body = new String(mensaje.getBody());
        System.out.println("Mensaje recibido en JSON: " + body);

        try {
            Class<?> eventClass = eventClassMap.get(routingKey);
            if (eventClass != null) {
                if (eventClass.equals(EventoBicicletaAlquilada.class)) {
                    EventoBicicletaAlquilada eventoBicicletaAlquilada = objectMapper.readValue(body, EventoBicicletaAlquilada.class);
                    LocalDateTime fecha = LocalDateTime.parse(eventoBicicletaAlquilada.getFecha());
                    servicio.bajaBicicleta(eventoBicicletaAlquilada.getIdBicicleta(), fecha, "Bicicleta alquilada");
                } else if (eventClass.equals(EventoBicicletaAlquilerConcluido.class)) {
                    EventoBicicletaAlquilerConcluido eventoAlquilerConcluido = objectMapper.readValue(body, EventoBicicletaAlquilerConcluido.class);
                    servicio.bicicletaDisponible(eventoAlquilerConcluido.getIdBicicleta());
                } else {
                    System.err.println("No se implementó el manejo para el tipo de evento: " + eventClass.getSimpleName());
                }
            } else {
                System.err.println("No se encontró una clase de evento para el routingKey: " + routingKey);
            }
        } catch (Exception e) {
            System.err.println("Error al procesar el mensaje: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
