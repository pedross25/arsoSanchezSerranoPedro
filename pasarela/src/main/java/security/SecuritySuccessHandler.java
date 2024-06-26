package security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class SecuritySuccessHandler implements AuthenticationSuccessHandler {

	private static final String SECRET_KEY = "secreto";

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		DefaultOAuth2User usuario = (DefaultOAuth2User) authentication.getPrincipal();

		Map<String, Object> claims = fetchUserInfo(usuario);
		if (claims != null) {
			// genera el token JWT y lo envía en la respuesta ...
			Date caducidad = Date.from(Instant.now().plusSeconds(60000));
			String token = Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS256, SECRET_KEY)
					.setExpiration(caducidad).compact();
			response.getWriter().append(token);

		} else {
			// notifica que no está autorizado en la aplicación
			response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "No autorizado");
            errorResponse.put("message", "No se pudo obtener la información del usuario.");
            response.getWriter().write(new ObjectMapper().writeValueAsString(errorResponse));
		}

	}

	private Map<String, Object> fetchUserInfo(DefaultOAuth2User usuario) {
		Map<String, Object> claims = new HashMap<>();
		claims.put ("sub","juan");
		claims.put("rol", "GESTOR");
		return claims;
	}
}