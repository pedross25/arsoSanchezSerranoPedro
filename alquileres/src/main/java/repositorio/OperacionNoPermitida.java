package repositorio;

public class OperacionNoPermitida extends Exception{

    private static final long serialVersionUID = 1L;
    public OperacionNoPermitida(String msg, Throwable causa){
        super(msg, causa);
    }
    public OperacionNoPermitida(String msg){
        super(msg);
    }
}
