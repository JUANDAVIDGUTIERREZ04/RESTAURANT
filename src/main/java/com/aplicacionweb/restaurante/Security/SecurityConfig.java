package com.aplicacionweb.restaurante.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UserDetailsImpl userDetailsImpl; // Referencia a tu servicio de detalles de usuario

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .csrf(csrf -> csrf.disable()) // Deshabilitar CSRF si es necesario (ten cuidado con esto)
        .authorizeHttpRequests(authorize -> authorize
            .requestMatchers("/registro","/detalle-plato/{menuId}" ,"/login", "/", "/css/**",
             "/js/**", "/registrar", "/images/**", "/modoInvitado","/prediccionReservas/**", "/imagenes/**","/adminMenu/listar", 
             "/reservas/registrar**","/reservas/**,/detalle-plato,").permitAll() // Permitir acceso a estas rutas sin autenticación
            .requestMatchers("/pedidos","/perfil","/restaurante").hasAnyRole("USER","ADMIN") // Solo admin puede acceder a rutas específicas
            .requestMatchers("/opcionAdmin","/cambiarEstado", "/mesas/**").hasRole("ADMIN") // Solo usuario normal
            .anyRequest().authenticated() // Resto de las rutas requieren autenticación
        )
        .formLogin(form -> form
            .loginPage("/login") // Página de inicio de sesión personalizada
            .defaultSuccessUrl("/restaurante", true) // Redirige a /perfil después del login exitoso (si es un usuario normal)
            .permitAll() // Permitir el acceso a la página de inicio de sesión
        )
        .logout(logout -> logout
            .logoutRequestMatcher(new AntPathRequestMatcher("/logout")) // Configuración de cierre de sesión
            .logoutSuccessUrl("/login?logout") // Redirigir tras logout
            .permitAll()
        );

    return http.build();
}


@Autowired
public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(userDetailsImpl); // Cargar usuarios desde la base de datos
}

@Bean
public PasswordEncoder passwordEncoder() {
    return new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder();
}

}








/*package com.aplicacionweb.restaurante.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @SuppressWarnings("deprecation")
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeRequests(authorize -> authorize
                .anyRequest().permitAll() // Permitir todas las solicitudes
            )
            .csrf(csrf -> csrf.disable()); // Deshabilitar CSRF si es necesario (cuidado con esto)

        return http.build();
    }
}



/*import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private ImplUserSeguridad implUserSeguridad;

    @Bean
    public AccesDenielHandler accesDenielHandler() {
        return new AccesDenielHandler();
    }

    @SuppressWarnings("removal")
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/public/**", "/", "/login/**", "/reservas","/css/**","/images/**","/jpg/**","/js/**","/registrar", "/registro/**", "/listaUser/**").permitAll() // Permitir rutas públicas
                .anyRequest().authenticated()
            )
            .formLogin()
                .loginProcessingUrl("/signin")
                .loginPage("/login").permitAll()
                .defaultSuccessUrl("/")
                .usernameParameter("username")
                .passwordParameter("password")
            .and()
            .exceptionHandling().accessDeniedHandler(accesDenielHandler())
            .and()
            .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout")) // Usar AntPathRequestMatcher
                .logoutSuccessUrl("/login?logout").permitAll()
                .deleteCookies("JSESSIONID")
            .and()
            .rememberMe().tokenValiditySeconds(360000).key("secret").rememberMeParameter("checkRememberme");

        return http.build();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(implUserSeguridad); // Usar la instancia inyectada
    }
}

*/