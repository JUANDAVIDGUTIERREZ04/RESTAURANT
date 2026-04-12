package com.aplicacionweb.restaurante.Service;

import com.aplicacionweb.restaurante.Models.Recarga;
import com.aplicacionweb.restaurante.Models.User;
import com.aplicacionweb.restaurante.Repository.RecargaRepository;
import com.aplicacionweb.restaurante.Repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class RecargaService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RecargaRepository recargaRepository;

    public boolean procesarRecarga(User user, BigDecimal monto, String metodo, HttpServletRequest request) {

        boolean aprobado = simularPago(metodo, request);

        // Inicializar saldo si es null
        if (user.getSaldo() == null) {
            user.setSaldo(BigDecimal.ZERO);
        }

        if (aprobado) {
            user.setSaldo(user.getSaldo().add(monto));
        }

        userRepository.save(user);

        // Guardar historial
        Recarga recarga = new Recarga();
        recarga.setMonto(monto);
        recarga.setMetodoPago(metodo);
        recarga.setEstado(aprobado ? "APROBADO" : "RECHAZADO");
        recarga.setFecha(LocalDateTime.now());
        recarga.setUsuario(user);

        recargaRepository.save(recarga);

        return aprobado;
    }

    private boolean simularPago(String metodo, HttpServletRequest request) {

        switch (metodo) {

            case "PSE":
                String banco = request.getParameter("banco");
                return banco != null && !banco.isEmpty();

            case "NEQUI":
                String otp = request.getParameter("otp");
                return "1234".equals(otp);

            case "TARJETA":
                String numero = request.getParameter("numeroTarjeta");
                return numero != null && numero.startsWith("4");

            case "TRANSFERENCIA":
                String comprobante = request.getParameter("comprobante");
                return comprobante != null && !comprobante.isEmpty();
        }

        return false;
    }
}