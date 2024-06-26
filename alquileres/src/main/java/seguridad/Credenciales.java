package seguridad;

public class Credenciales {
	private String usuario;
	private String password;
	private String role;

	public Credenciales(String id, String password, String role) {
		this.usuario = id;
		this.password = password;
		this.role = role;
	}

	public String getId() {
		return usuario;
	}

	public String getPassword() {
		return password;
	}

	public void setId(String id) {
		this.usuario = id;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

}