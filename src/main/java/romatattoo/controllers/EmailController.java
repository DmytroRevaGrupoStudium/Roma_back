package romatattoo.controllers;

import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import romatattoo.services.EmailService;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Configuration
public class EmailController {

    // Inyectamos clase Service
    @Autowired
    private EmailService emailService;

    @Value("${app.company}")
    private String companyName;

    // Método para configurar la plantilla de correo y enviarlo a base de Service
    public String sendEmail(String to, String name, String subject, String motivo) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("name", name);
        variables.put ("motivo", motivo);
        variables.put ("companyName", companyName);
        variables.put ("email", to);

        try {
            emailService.sendEmail(to, subject, "email", variables);
        } catch (MessagingException e) {
            e.printStackTrace();
            return "Error al enviar email";
        }

        return "Email enviado correctamente";
    }
}
