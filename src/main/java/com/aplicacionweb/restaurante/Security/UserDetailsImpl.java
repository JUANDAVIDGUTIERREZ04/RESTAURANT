package com.aplicacionweb.restaurante.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.aplicacionweb.restaurante.Models.User; // Importa tu modelo User
import com.aplicacionweb.restaurante.Repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails; 
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.List;

@Service
public class UserDetailsImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository; // Repositorio para acceder a los datos

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username); // Buscar usuario por nombre
        if (user == null) {
            throw new UsernameNotFoundException("Usuario no encontrado");
        }

        // Convertir el rol a un GrantedAuthority, asegurándonos de que el rol tiene el prefijo ROLE_
        return new org.springframework.security.core.userdetails.User(
            user.getUsername(),
            "{noop}" + user.getPassword(), // Contraseña sin encriptar, con {noop} como prefijo
            Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole())) // Asegurarse de que el rol tiene el prefijo ROLE_
        );
    }

    // Clase CustomUserDetails no es necesaria si no la utilizas, pero la dejo aquí por si deseas personalizarla.
    public static class CustomUserDetails implements UserDetails {
        private final User user;

        public CustomUserDetails(User user) {
            this.user = user;
        }

        @Override
        public String getUsername() {
            return user.getUsername();
        }

        @Override
        public String getPassword() {
            return user.getPassword();
        }

        @Override
        public boolean isAccountNonExpired() {
            return true; // Cambia según tu lógica
        }

        @Override
        public boolean isAccountNonLocked() {
            return true; // Cambia según tu lógica
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return true; // Cambia según tu lógica
        }

        @Override
        public boolean isEnabled() {
            return true; // Cambia según tu lógica
        }

        @Override
        public List<SimpleGrantedAuthority> getAuthorities() {
            // Aquí asignamos el rol con el prefijo ROLE_
            return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole()));
        }

        public Long getId() {
            return user.getId(); // Obtener el ID del usuario, por si lo necesitas
        }
    }
}