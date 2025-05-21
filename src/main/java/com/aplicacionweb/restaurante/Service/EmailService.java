package com.aplicacionweb.restaurante.Service;



import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }


    public void sendEmail(SimpleMailMessage message) {
    try {
        javaMailSender.send(message);
        System.out.println("Correo enviado con éxito");
    } catch (Exception e) {
        System.err.println("Error al enviar el correo: " + e.getMessage());
        e.printStackTrace();  // Esto imprimirá el error completo en la consola para depurar
    }
}

}
