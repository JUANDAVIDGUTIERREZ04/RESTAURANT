package com.aplicacionweb.restaurante.Repository;




import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import com.aplicacionweb.restaurante.Models.User;



@Repository
public interface UserRepository extends JpaRepository<User,Long > {
    //buscar usuario por nombre
    
    User findByUsername(String username);
    //buscar usuario por correo
    boolean existsByUsername(String username);

    
} 
    

