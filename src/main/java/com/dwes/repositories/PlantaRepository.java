package com.dwes.repositories;

import com.dwes.models.Planta;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PlantaRepository extends JpaRepository<Planta, Long> {
    List<Planta> findAllByOrderByNombreComunAsc(); // Ordena por nombre común ascendente
    boolean existsByCodigo(String codigo); // Verifica si el código ya existe
    boolean existsByNombreComun(String nombreComun); // Verifica nombre común único
    boolean existsByNombreCientifico(String nombreCientifico); // Verifica nombre científico único
}
