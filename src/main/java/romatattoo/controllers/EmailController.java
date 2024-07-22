package romatattoo.controllers;

import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import romatattoo.services.EmailService;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class EmailController {

    @Autowired
    private EmailService emailService;

    public String sendEmail(String to, String name) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("name", name);
        variables.put("details", Map.of("Confirmación de cuenta", "código de confirmación es: 1234"));

        try {
            emailService.sendEmail(to, "Test Email", "email", variables);
        } catch (MessagingException e) {
            e.printStackTrace();
            return "Error while sending email";
        }

        return "Email sent successfully";
    }
}
