package romatattoo.controllers;

import romatattoo.entities.Tatuaje;
import romatattoo.services.TatuajeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tatuajes")
public class TatuajeController {
    private final TatuajeService tatuajeService;

    @Autowired
    public TatuajeController(TatuajeService tatuajeService) {
        this.tatuajeService = tatuajeService;
    }

    @GetMapping
    public List<Tatuaje> obtenerTodosLosTatuajes() {
        return tatuajeService.obtenerTodosLosTatuajes();
    }

    @GetMapping("/{id}")
    public Tatuaje obtenerTatuajePorId(@PathVariable Long id) {
        return tatuajeService.obtenerTatuajePorId(id);
    }

    @PostMapping
    public Tatuaje crearTatuaje(@RequestBody Tatuaje tatuaje) {
        return tatuajeService.crearTatuaje(tatuaje);
    }

    @PutMapping("/{id}")
    public Tatuaje actualizarTatuaje(@PathVariable Long id, @RequestBody Tatuaje tatuaje) {
        return tatuajeService.actualizarTatuaje(id, tatuaje);
    }

    @DeleteMapping("/{id}")
    public void eliminarTatuaje(@PathVariable Long id) {
        tatuajeService.eliminarTatuaje(id);
    }
}