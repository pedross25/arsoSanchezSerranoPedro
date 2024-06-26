package security;

import io.jsonwebtoken.*;
import model.UserDetails;

import javax.xml.bind.DatatypeConverter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtUtils {

    private static final String SECRET_KEY = "secreto";

    public static String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<String, Object>();
        claims.put("name", userDetails.getName());
        claims.put("rol", userDetails.getRol());

        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 1 día
                .compact();

    }

    public static Claims validateJWT(String jwt) throws ExpiredJwtException, UnsupportedJwtException,
            MalformedJwtException, SignatureException, IllegalArgumentException{

        return Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(SECRET_KEY))
                .parseClaimsJws(jwt).getBody();
    }
}
