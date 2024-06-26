package seguridad;

import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.HashMap;

import javax.annotation.security.PermitAll;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Path("/alquileres/auth")
public class ControladorAuth {
	
	private IUsuarioService usuarioService = new UsuarioService();

	@POST
	@Path("/login")
	@PermitAll
	public Response login(@FormParam("username") String username, @FormParam("password") String password) {

		Map<String, Object> claims = usuarioService.verificarCredenciales(username, password);
		if (claims != null) {
			Date caducidad = Date.from(Instant.now().plusSeconds(3600)); // 1 hora de validez
			String token = Jwts.builder()
					.setClaims(claims)
					.signWith(SignatureAlgorithm.HS256, "secreto")
					.setExpiration(caducidad).compact();

			return Response.ok(token).build();
		} else {
			return Response.status(Response.Status.UNAUTHORIZED).entity("Credenciales inválidas").build();
		}

	}

}
