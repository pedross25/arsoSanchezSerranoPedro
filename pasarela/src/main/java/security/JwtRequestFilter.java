package security;

import io.jsonwebtoken.Claims;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private static final String PREFIX = "Bearer ";

    private static final String ROL = "rol";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        String jwt = null;

        // cookies
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals("jwt"))
                    jwt = cookie.getValue();
            }
        }

        if (jwt == null) {
            final String authHeader = request.getHeader("Authorization");
            if (authHeader != null && authHeader.startsWith(PREFIX))
                jwt = authHeader.substring(PREFIX.length());
        }

        if (jwt != null) {
            Claims claims = JwtUtils.validateJWT(jwt);

            ArrayList<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
            authorities.add(new SimpleGrantedAuthority(claims.get(ROL).toString()));

            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(claims.getSubject(),
                    null, authorities);

            // Establecemos la autenticación en el contexto de seguridad
            // Se interpreta como que el usuario ha superado la autenticación

            SecurityContextHolder.getContext().setAuthentication(auth);

        }

        chain.doFilter(request, response);
    }

}