package com.dwes.repositories;

import com.dwes.models.Planta;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PlantaRepository extends JpaRepository<Planta, Long> {
    List<Planta> findAllByOrderByNombreComunAsc();
    boolean existsByCodigo(String codigo);
    boolean existsByNombreComun(String nombreComun);
    boolean existsByNombreCientifico(String nombreCientifico);
}
