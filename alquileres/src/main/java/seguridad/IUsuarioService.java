package seguridad;

import java.util.Map;

public interface IUsuarioService {
	
	Map<String, Object> verificarCredenciales(String username, String password);

}
