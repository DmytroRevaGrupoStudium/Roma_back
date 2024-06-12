package romatattoo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import romatattoo.entities.Informacion;
@Repository
public interface InformacionRepository extends JpaRepository<Informacion, Long> {}
