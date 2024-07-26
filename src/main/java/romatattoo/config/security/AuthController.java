package romatattoo.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import romatattoo.controllers.EmailController;
import romatattoo.entities.UserTienda;
import romatattoo.services.UserTiendaService;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    // Creamos objetos para cumplir con servicio de autenticación
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserTiendaService userTiendaService;
    private final PasswordEncoder passwordEncoder;
    private final EmailController emailController;

    // Inyectamos los objetos
    @Autowired
    public AuthController(AuthenticationManager authenticationManager, JwtService jwtService, UserTiendaService userTiendaService, PasswordEncoder passwordEncoder, EmailController emailController) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userTiendaService = userTiendaService;
        this.passwordEncoder = passwordEncoder;
        this.emailController = emailController;
    }

    // Métodos REST para los procedimientos relacionados con la autenticaciones
    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody UserTienda userTienda) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userTienda.getEmail(), userTienda.getPassword())
            );
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("message", "Credenciales incorrectas"));
        }

        // Si no salta el catch, cargamos datos de usuario y generamos su token correspondiente
        final UserDetails userDetails = jwtService.loadUserByUsername(userTienda.getEmail());

        // Si cuenta está en orden, creamos token
        final String jwt = jwtService.generateToken(userDetails);

        // Devuelve el token dentro de un objeto JSON
        return ResponseEntity.ok(Collections.singletonMap("token", jwt));
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserTienda userTienda) {

        // Validar si el usuario ya existe
        if (userTiendaService.existsByEmail(userTienda.getEmail())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("message", "Email ya está en uso"));
        }

        // Enviamos el email con confirmación de cuenta
        emailController.sendEmail(userTienda.getEmail(), userTienda.getNombre(), "Registro", "Bienvenido, para activar su cuenta acceda a siguiente enlace:");

        // Encriptar la contraseña antes de guardarla en la base de datos
        userTienda.setPassword(passwordEncoder.encode(userTienda.getPassword()));

        // Guardar el usuario en la base de datos
        userTiendaService.save(userTienda);

        // Devolver una respuesta de éxito
        return ResponseEntity.ok(Collections.singletonMap("message", "Usuario creado correctamente"));
    }

    @GetMapping("/reset_password")
    public ResponseEntity<Map<String, String>> resetearClave(@RequestParam("email") String email, @RequestParam("password") String password) {

        try {
            // Buscamos a user
            Optional<UserTienda> optionalUser = userTiendaService.obtenerUserTiendaByEmail(email);

            // Confirmamos que user está en BD
            if (optionalUser.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("message", "Error, usuario no existe."));
            }
            else
            {
                // Capturamos objeto en una variable
                UserTienda userTienda = optionalUser.get();

                // Comprobamos que nueva contraseña no puede ser la misma que la actual
                if (passwordEncoder.matches(password, userTienda.getPassword()))
                {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("message", "Nueva contraseña no puede ser igual a la anterior."));
                }

                // Si contraseña es válida, encriptamos la contraseña
                userTienda.setPassword(passwordEncoder.encode(password));

                // Actualizamos al user
                userTiendaService.save(userTienda);

                // Respuesta de éxito con formato JSON
                Map<String, String> response = Collections.singletonMap("message", "¡Contraseña actualizada correctamente!");
                return ResponseEntity.ok(response);
            }
        }
        catch (Exception e) {
            // Crear el mensaje de error
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Error durante la actualización de contraseña");

            // Devolver una respuesta con el mensaje de error en formato JSON
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}