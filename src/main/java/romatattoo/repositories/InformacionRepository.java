package romatattoo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import romatattoo.entities.Informacion;

// Repositorio de tabla Información
@Repository
public interface InformacionRepository extends JpaRepository<Informacion, Long> {}
