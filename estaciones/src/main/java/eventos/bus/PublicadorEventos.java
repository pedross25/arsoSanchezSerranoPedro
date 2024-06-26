package eventos.bus;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import eventos.config.RabbitMQConfig;

@Component
public class PublicadorEventos {

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public PublicadorEventos(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void emitirEvento(Object evento) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.ROUTING_KEY, evento);
    }

}