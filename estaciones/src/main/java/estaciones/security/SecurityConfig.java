package estaciones.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private SecuritySuccessHandler successHandler;

    @Autowired
    private JwtRequestFilter authenticationRequestFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                .antMatchers("/estaciones/**").permitAll()
                .antMatchers(HttpMethod.POST, "/estaciones/bicicletas/estacionar/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .oauth2Login().successHandler(successHandler)
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // Establece el filtro de autenticación en la cadena de filtros de seguridad
        http.addFilterBefore(authenticationRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }
}

