package seguridad;

import java.util.HashMap;
import java.util.Map;

public class UsuarioService implements IUsuarioService{
	
    private static final Map<String, Credenciales> USUARIOS = new HashMap<>();

    static {
    	Credenciales usuario1 = new Credenciales("user1", "123", "usuario");
        Credenciales usuario2 = new Credenciales("gestor", "456", "gestor");

        USUARIOS.put(usuario1.getId(), usuario1);
        USUARIOS.put(usuario2.getId(), usuario2);
    }

	@Override
	public Map<String, Object> verificarCredenciales(String username, String password) {
		Map<String, Object> claims = new HashMap<String, Object>();
		
		Credenciales usuario = USUARIOS.get(username);
        if (usuario != null && usuario.getPassword().equals(password)) {
        	claims.put("sub", username);
    		claims.put("roles", usuario.getRole());
			return claims;
        }
		return null;
	}

}
