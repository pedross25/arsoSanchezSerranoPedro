package controller;

import model.AuthResponse;
import model.LoginRequest;
import model.OAuth2Request;
import model.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import security.JwtUtils;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import java.util.Objects;

import static controller.Routes.VERIFY_CREDENTIALS;
import static controller.Routes.VERIFY_OAUTH2;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private RestTemplate restTemplate;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        // Petición al servicio Usuarios para verificar credenciales
        try {
            ResponseEntity<UserDetails> response = restTemplate.postForEntity(VERIFY_CREDENTIALS,
                    loginRequest,
                    UserDetails.class
            );
            if (response.getStatusCode() == HttpStatus.OK) {
                UserDetails userDetails = response.getBody();
                if (userDetails != null) {
                    String token = JwtUtils.generateToken(userDetails);
                    return ResponseEntity.ok(new AuthResponse(token, userDetails.getUserId(), userDetails.getName(), userDetails.getRol()));
                }
            }
        } catch (HttpClientErrorException.NotFound ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Credenciales incorrectas");
        } catch (HttpServerErrorException ex) {
            return ResponseEntity.status(ex.getRawStatusCode()).build();
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @GetMapping("/oauth2")
    public ResponseEntity<?> oauth2Login(@AuthenticationPrincipal OAuth2User oAuth2User) {
        if (oAuth2User == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String userId = Objects.requireNonNull(oAuth2User.getAttribute("id")).toString();
        OAuth2Request oauth2Request = new OAuth2Request(userId);
        try {
            ResponseEntity<UserDetails> response = restTemplate.postForEntity(VERIFY_OAUTH2, oauth2Request, UserDetails.class);
            UserDetails userDetails = response.getBody();
            if (userDetails != null) {
                String token = JwtUtils.generateToken(userDetails);
                return ResponseEntity.ok(new AuthResponse(token, userDetails.getUserId(), userDetails.getName(), userDetails.getRol()));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error verifying OAuth2 credentials");
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error verifying OAuth2 credentials");
    }
}

