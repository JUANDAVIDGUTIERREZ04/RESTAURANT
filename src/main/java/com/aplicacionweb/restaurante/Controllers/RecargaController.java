package com.aplicacionweb.restaurante.Controllers;

import com.aplicacionweb.restaurante.Models.User;
import com.aplicacionweb.restaurante.Repository.UserRepository;
import com.aplicacionweb.restaurante.Service.RecargaService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.security.Principal;

@Controller
public class RecargaController {

    @Autowired
    private RecargaService recargaService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/recargar")
    public String recargar(
            @RequestParam BigDecimal monto,
            @RequestParam String metodoPago,
            HttpServletRequest request,
            Principal principal
    ) {

        User user = userRepository
                .findOptionalByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        boolean ok = recargaService.procesarRecarga(user, monto, metodoPago, request);

        if (ok) {
            return "redirect:/perfil?exito";
        } else {
            return "redirect:/recarga?error";
        }
    }

    @GetMapping("/recarga")
    public String mostrarFormulario() {
        return "recarga"; // recarga.html
    }
}