package com.aplicacionweb.restaurante.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {

    @GetMapping("/opcionAdmin")
    public String opcionAdmin(){

        return "opciones_admin";

    }
    
}
