package romatattoo.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "tatuajes")
public class Tatuaje {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre_tatuaje")
    private String nombreTatuaje;

    @Column(name = "descripcion")
    private String descripcion;

    @ElementCollection
    @CollectionTable(name = "tatuaje_imagenes", joinColumns = @JoinColumn(name = "tatuaje_id"))
    @Column(name = "imagen", columnDefinition = "LONGTEXT")
    private List<String> imagenes;
}