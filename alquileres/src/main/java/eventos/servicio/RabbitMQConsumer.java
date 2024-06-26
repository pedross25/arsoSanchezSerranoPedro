package eventos.servicio;

import java.io.IOException;

import com.rabbitmq.client.*;

import com.rabbitmq.client.*;

public class RabbitMQConsumer {

	private static final String EXCHANGE_NAME = "citybike";
	private static final String QUEUE_NAME = "citybike-alquileres";
	//private static final String BINDING_KEY = "#.alquileres";
	private static final String BINDING_KEY = "citybike.estaciones.*";

	public static void main(String[] argv) throws Exception {
		// Conexión al servidor RabbitMQ
		ConnectionFactory factory = new ConnectionFactory();
		factory.setUri("amqps://kdqwtupx:Y5rUUkkKEEf5zx0c-CYTfBPki-3iUSx2@rat.rmq2.cloudamqp.com/kdqwtupx");

		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();

		// Declarar el exchange y la cola
		channel.exchangeDeclare(EXCHANGE_NAME, "topic", true);
		channel.queueDeclare(QUEUE_NAME, true, false, false, null);
		channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, BINDING_KEY);

		// Configurar el consumidor
		System.out.println(" [*] Esperando mensajes. Para salir presiona CTRL+C");
		
		DeliverCallback deliverCallback = (consumerTag, delivery) -> {
			String message = new String(delivery.getBody(), "UTF-8");
			System.out.println(" [x] Recibido '" + message + "'");
		};

//		channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> {
//		});
//		
		//channel.queueDeclare(queueName, durable, exclusive, autodelete, properties);
		//channel.queueBind(queueName, EXCHANGE_NAME, bindingKey);
		
		channel.basicConsume(QUEUE_NAME, true, new DefaultConsumer(channel) {
		    @Override
		    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
		            byte[] body) throws IOException {
		        String message = new String(body, "UTF-8");
		        System.out.println("Recibido mensaje: " + message); // Imprimir el mensaje recibido
		        // eventConsumer.handleEvent(message); // Procesar el mensaje, si es necesario
		    }
		});


//		channel.basicConsume(QUEUE_NAME, true, new DefaultConsumer(channel) {
//			@Override
//			public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
//					byte[] body) throws IOException {
//				String message = new String(body, "UTF-8");
//				//eventConsumer.handleEvent(message);
//			}
//		});
	}
}
