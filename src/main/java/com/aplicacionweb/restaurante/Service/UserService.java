package com.aplicacionweb.restaurante.Service;

import com.aplicacionweb.restaurante.Models.DTO.UserDto;
import com.aplicacionweb.restaurante.Repository.UserRepository;
import com.aplicacionweb.restaurante.Models.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    

    

    // Guardar usuario
    public User saveUser(User user) {
       
        return userRepository.save(user);
    }

    // Buscar usuario por nombre de usuario
    public User buscarByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    // Obtener todos los usuarios como DTO
    public List<UserDto> obtenerTodosLosUsuarios() {
        return userRepository.findAll().stream()
                .map(user -> {
                    UserDto dto = new UserDto();
                    dto.setId(user.getId());
                    dto.setNombre(user.getNombre());
                    dto.setCorreo(user.getCorreo());
                    dto.setEdad(user.getEdad());
                    dto.setSexo(user.getSexo());
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

    // Obtener usuario por ID
    public Optional<User> getById(Long id) {
        return userRepository.findById(id);
    }

    // Verificar si el usuario existe por ID
    public boolean exitById(Long id) {
        return userRepository.existsById(id);
    }

    // Verificar si el usuario existe por nombre
    public boolean existsByNombre(String username) {
        return userRepository.existsByUsername(username);
    }

    // Actualizar usuario
    public void updateUser(Long id, User updatedUser) {
        if (userRepository.existsById(id)) {
            updatedUser.setId(id);
            
            userRepository.save(updatedUser);
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

    // Obtener todos los usuarios activos como DTO
public List<UserDto> obtenerUsuariosActivos() {
    return userRepository.findByActivoTrue().stream()
            .map(user -> {
                UserDto dto = new UserDto();
                dto.setId(user.getId());
                dto.setNombre(user.getNombre());
                dto.setCorreo(user.getCorreo());
                dto.setEdad(user.getEdad());
                dto.setSexo(user.getSexo());
                dto.setTelefono(user.getTelefono());
                dto.setUsername(user.getUsername());
                dto.setRole(user.getRole());
                return dto;
            })
            .collect(Collectors.toList());
}





// Marcar usuario como inactivo
    public void marcarUsuarioInactivo(Long id) {
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setActivo(false);  // Cambiamos el estado de 'activo' a false
            userRepository.save(user);  // Guardamos la entidad actualizada
        }
    }


    public Page<UserDto> obtenerUsuariosActivosPaginados(Pageable pageable) {
    return userRepository.findByActivoTrue(pageable)
        .map(user -> {
            UserDto dto = new UserDto();
            dto.setId(user.getId());
            dto.setNombre(user.getNombre());
            dto.setCorreo(user.getCorreo());
            dto.setEdad(user.getEdad());
            dto.setSexo(user.getSexo());
            dto.setTelefono(user.getTelefono());
            dto.setUsername(user.getUsername());
            dto.setRole(user.getRole());
            return dto;
        });
}


public Page<UserDto> buscarUsuariosPorIdConPaginacion(Long userId, Pageable pageable) {
    return userRepository.findById(userId, pageable) // Aquí estamos buscando por ID, ajusta según el caso de uso.
        .map(user -> {
            UserDto dto = new UserDto();
            dto.setId(user.getId());
            dto.setNombre(user.getNombre());
            dto.setCorreo(user.getCorreo());
            dto.setEdad(user.getEdad());
            dto.setSexo(user.getSexo());
            dto.setTelefono(user.getTelefono());
            dto.setUsername(user.getUsername());
            dto.setRole(user.getRole());
            return dto;
        });
}


}
