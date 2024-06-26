package alquileres.servicio;

public class ServicioAlquileresException extends Exception {

	private static final long serialVersionUID = 1L;
	public ServicioAlquileresException(String msg, Throwable causa){
        super(msg, causa);
    }
    public ServicioAlquileresException(String msg){
        super(msg);
    }

}