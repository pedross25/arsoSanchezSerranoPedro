package seguridad;

import java.util.Arrays;
import java.util.Date;
import java.util.Set;
import java.util.HashSet;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.annotation.security.PermitAll;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@Provider
@Priority(Priorities.AUTHENTICATION)
public class JwtTokenFilter implements ContainerRequestFilter {

	@Context
	private ResourceInfo resourceInfo;

	@Context
	private HttpServletRequest servletRequest;

	@Override
	public void filter(ContainerRequestContext requestContext) {
		// Comprobamos si la ruta tiene la anotación @PermitAll
		if (resourceInfo.getResourceMethod().isAnnotationPresent(PermitAll.class)) {
			return;
		}

		// Implementación del control de autorización
		String authorization = requestContext.getHeaderString("Authorization");
		if (authorization == null || !authorization.startsWith("Bearer ")) {
			requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
		} else {
			String token = authorization.substring("Bearer ".length()).trim();
			try {
				Claims claims = Jwts.parser().setSigningKey("secreto").parseClaimsJws(token).getBody();
				this.servletRequest.setAttribute("claims", claims);
				Date caducidad = claims.getExpiration();
				if (caducidad.before(new Date()))
					requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());

				Set<String> roles = new HashSet<>(Arrays.asList(claims.get("roles", String.class).split(",")));
				
				
				// Consulta si la operación está protegida por rol
				if (this.resourceInfo.getResourceMethod().isAnnotationPresent(RolesAllowed.class)) {
					String[] allowedRoles = resourceInfo.getResourceMethod().getAnnotation(RolesAllowed.class).value();
					if (roles.stream().noneMatch(userRole -> Arrays.asList(allowedRoles).contains(userRole))) {
						requestContext.abortWith(Response.status(Response.Status.FORBIDDEN).build());
					}
				}

			} catch (Exception e) { // Error de validación
				requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
			}
		}
	}
}