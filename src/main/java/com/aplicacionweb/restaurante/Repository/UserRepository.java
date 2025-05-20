package com.aplicacionweb.restaurante.Repository;




import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import com.aplicacionweb.restaurante.Models.User;



@Repository
public interface UserRepository extends JpaRepository<User,Long > {
    //buscar usuario por nombre
    
    User findByUsername(String username);
    //buscar usuario por correo
    boolean existsByUsername(String username);

     // Buscar usuarios activos (estado = true)
    List<User> findByActivoTrue();  // Esto devuelve solo los usuarios activos
     // Buscar usuarios activos (estado = true)
    List<User> findByActivoFalse();  // Esto devuelve solo los usuarios activos

  Page<User> findByActivoTrue(Pageable pageable);

  Page<User> findById(Long userId, Pageable pageable);
  
} 
    

