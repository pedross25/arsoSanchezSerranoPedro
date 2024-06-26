package eventos.servicio;

@FunctionalInterface
public interface EventConsumer {
    void handleEvent(String message);
}
