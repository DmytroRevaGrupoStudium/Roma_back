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
import romatattoo.entities.UserTienda;
import romatattoo.services.UserTiendaService;

import java.util.Collections;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserTiendaService userTiendaService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, JwtService jwtService, UserTiendaService userTiendaService, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userTiendaService = userTiendaService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody UserTienda userTienda) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userTienda.getEmail(), userTienda.getPassword())
            );
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }

        final UserDetails userDetails = jwtService.loadUserByUsername(userTienda.getEmail());
        final String jwt = jwtService.generateToken(userDetails);

        // Devuelve el token dentro de un objeto JSON
        return ResponseEntity.ok(Collections.singletonMap("token", jwt));
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserTienda userTienda) {
        // Validar si el usuario ya existe
        if (userTiendaService.existsByEmail(userTienda.getEmail())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("message", "Email already in use"));
        }

        // Encriptar la contraseña antes de guardarla en la base de datos
        userTienda.setPassword(passwordEncoder.encode(userTienda.getPassword()));

        // Guardar el usuario en la base de datos
        userTiendaService.save(userTienda);

        // Devolver una respuesta de éxito
        return ResponseEntity.ok(Collections.singletonMap("message", "Usuario creado correctamente"));
    }
}