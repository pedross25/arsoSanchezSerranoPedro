package eventos.servicio;

import java.io.IOException;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Envelope;

import eventos.Evento;

public class EventBusService implements IEventBusService {

	private final static String EXCHANGE_NAME = "citybike";
	//private final static String AMQP_URI = "amqps://kdqwtupx:Y5rUUkkKEEf5zx0c-CYTfBPki-3iUSx2@rat.rmq2.cloudamqp.com/kdqwtupx";

	//private final static String AMQP_URI = System.getenv("RABBITMQ_URI");
	//private final static String AMQP_URI = ("amqp://guest:guest@rabbitmq:5672/");
	private final static String AMQP_URI = "amqp://guest:guest@localhost:5672/";



	@Override
	public void crearExchange() {
		ConnectionFactory factory = new ConnectionFactory();

		try {
			factory.setUri(AMQP_URI);
			Connection connection = factory.newConnection();
			Channel channel = connection.createChannel();

			boolean durable = true;
			channel.exchangeDeclare(EXCHANGE_NAME, "topic", durable);

			channel.close();
			connection.close();

		} catch (Exception e) {

		}

	}

	public void notificarEvento(String routingKey, Object eventData) throws Exception {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setUri(AMQP_URI);
		try (Connection connection = factory.newConnection();
			 Channel channel = connection.createChannel()) {

			ObjectMapper mapper = new ObjectMapper();
			String mensaje = mapper.writeValueAsString(eventData);

			AMQP.BasicProperties props = new AMQP.BasicProperties.Builder()
					.contentType("application/json")
					.build();

			channel.basicPublish(EXCHANGE_NAME, routingKey, props, mensaje.getBytes("UTF-8"));
		}
	}


	@Override
	public void consumirEvento(String queueName, String bindingKey, EventConsumer eventConsumer)
			throws IOException, TimeoutException {

		// Conexión al servidor RabbitMQ
		ConnectionFactory factory = new ConnectionFactory();
		try {
			factory.setUri(AMQP_URI);
		} catch (KeyManagementException | NoSuchAlgorithmException | URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();

		channel.exchangeDeclare(EXCHANGE_NAME, "topic", true);
		channel.queueDeclare(queueName, true, false, false, null);
		channel.queueBind(queueName, EXCHANGE_NAME, bindingKey);

		DeliverCallback deliverCallback = (consumerTag, delivery) -> {
			String message = new String(delivery.getBody(), "UTF-8");
			System.out.println(" [x] Recibido '" + message + "'");
		};

		channel.basicConsume(queueName, true, new DefaultConsumer(channel) {
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
									   byte[] body) throws IOException {
				String message = new String(body, "UTF-8");
				eventConsumer.handleEvent(message);
			}
		});
	}

}
