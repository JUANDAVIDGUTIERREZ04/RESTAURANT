package com.aplicacionweb.restaurante.Models.DTO;

import java.io.Serializable;

public class UserDto implements Serializable{

    public Long id;

   
    public String nombre;

    
    public String correo;

    
    public String telefono;

    
    public String username;

    public int getEdad() {
        return edad;
    }


    public void setEdad(int edad) {
        this.edad = edad;
    }


    public String getSexo() {
        return Sexo;
    }


    public void setSexo(String sexo) {
        Sexo = sexo;
    }


    public String role;

    public int edad;

    public String Sexo;


    public String getRole() {
        return role;
    }


    public void setRole(String role) {
        this.role = role;
    }


    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public String getNombre() {
        return nombre;
    }


    public void setNombre(String nombre) {
        this.nombre = nombre;
    }


    public String getCorreo() {
        return correo;
    }


    public void setCorreo(String correo) {
        this.correo = correo;
    }


    public String getTelefono() {
        return telefono;
    }


    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }


    public String getUsername() {
        return username;
    }


    public void setUsername(String username) {
        this.username = username;
    }


    

    
    }

    
    
    
    
   



