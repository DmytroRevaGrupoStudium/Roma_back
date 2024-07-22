package romatattoo.controllers;

import romatattoo.entities.UserTienda;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import romatattoo.services.UserTiendaService;

import java.util.Optional;

@RestController
@RequestMapping("/api/user_tienda")
public class UserTiendaController {

    // Creación y inyección de servicio
    private final UserTiendaService userTiendaService;

    @Autowired
    public UserTiendaController(UserTiendaService userTiendaService) {
        this.userTiendaService = userTiendaService;
    }

    // Métodos REST para gestión de tabla de usuarios
    @GetMapping("/{email}")
    public Optional<UserTienda> obtenerUserTiendaByEmail(@PathVariable String email) {
        return userTiendaService.obtenerUserTiendaByEmail(email);
    }
}