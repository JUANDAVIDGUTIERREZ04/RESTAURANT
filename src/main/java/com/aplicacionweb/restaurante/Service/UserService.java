package com.aplicacionweb.restaurante.Service;

import com.aplicacionweb.restaurante.Models.User;
import com.aplicacionweb.restaurante.Models.DTO.UserDto;
import com.aplicacionweb.restaurante.Repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User saveUser(User user) {
        return userRepository.save(user); // Guardar el usuario
    }

    public User buscarByUsername(String username) {
        return userRepository.findByUsername(username); // Implementa este método en tu UserRepository
    }
    


    public List<UserDto> obtenerTodosLosUsuarios() {
        return userRepository.findAll().stream()
            .map(user -> {
                UserDto dto = new UserDto();
                dto.setId(user.getId());
                dto.setNombre(user.getNombre());
                dto.setCorreo(user.getCorreo());
                dto.setTelefono(user.getTelefono());
                dto.setUsername(user.getUsername());
                dto.setRole(user.getRole());
                return dto;
            })
            .collect(Collectors.toList());
    }



    // Eliminar usuario por id
    public void eliminarUsuario(Long id) {
        userRepository.deleteById(id);
    }

    public Optional<User> getById(Long id) {
        return userRepository.findById((long) id);
    }

    public boolean exitById(Long id) {
        return userRepository.existsById(id);
    }

    public boolean existsByNombre(String username) {
        return userRepository.existsByUsername(username);
    }

    // Método para actualizar un usuario
    public void updateUser(Long id, User updatedUser) {
        if (userRepository.existsById(id)) {
            updatedUser.setId(id); // Asegúrate de que el ID sea correcto
            userRepository.save(updatedUser); // Guarda el usuario modificado
        }
    }

        // Buscar usuario por ID y devolver como DTO
    public UserDto buscarUsuarioPorId(Long id) {
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            UserDto userDto = new UserDto();
            userDto.setId(user.getId());
            userDto.setNombre(user.getNombre());
            userDto.setCorreo(user.getCorreo());
            userDto.setTelefono(user.getTelefono());
            userDto.setUsername(user.getUsername());
            userDto.setRole(user.getRole());
            return userDto;
        }
        return null;
    
    

}

     // Obtener todos los usuarios y mapearlos a DTOs
    

     // Método para obtener el usuario actualmente autenticado por ID
   /*   public User getUsuarioActual() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            Long userId = (Long) auth.getPrincipal(); // Suponiendo que el principal es el ID del usuario
            return userRepository.findById(userId).orElse(null);
        }
        return null;
    }
        */
}
