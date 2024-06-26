package security;

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
                .antMatchers(HttpMethod.POST, "/alquileres/reservar").permitAll()
                .antMatchers("/auth/login", "/auth/oauth2").permitAll()
                .antMatchers("/usuarios/**").permitAll()
                .antMatchers("/alquileres/**").permitAll()
                .antMatchers("/estaciones/**").permitAll()
                .antMatchers(HttpMethod.POST, "/usuarios/solicitar-codigo-activacion/**").hasRole("GESTOR")
                .antMatchers("/usuarios/baja/**").hasRole("GESTOR")
                .antMatchers("/usuarios/listar/**").hasRole("GESTOR")
                .anyRequest().authenticated()
                .and()
                //.oauth2Login().successHandler(successHandler)
                //.and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.addFilterBefore(authenticationRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }
}

