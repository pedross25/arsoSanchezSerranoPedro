package estaciones.repositorio;

public class EntidadNoEncontrada extends Exception{

	private static final long serialVersionUID = 1L;
	public EntidadNoEncontrada(String msg, Throwable causa){
        super(msg, causa);
    }
    public EntidadNoEncontrada(String msg){
        super(msg);
    }
}
