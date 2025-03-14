package com.dwes.repositories;

import com.dwes.models.Mensaje;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.time.LocalDateTime;
import java.util.List;

public interface MensajeRepository extends JpaRepository<Mensaje, Long> {

    @Query("SELECT m FROM Mensaje m WHERE m.persona.id = :personaId")
    List<Mensaje> findByPersonaId(Long personaId);

    @Query("SELECT m FROM Mensaje m WHERE m.ejemplar.id = :ejemplarId")
    List<Mensaje> findByEjemplarId(Long ejemplarId);

    @Query("SELECT m FROM Mensaje m WHERE m.fechaHora BETWEEN :inicio AND :fin")
    List<Mensaje> findByFechaHoraBetween(LocalDateTime inicio, LocalDateTime fin);

    @Query("SELECT m FROM Mensaje m WHERE m.ejemplar.id = :ejemplarId ORDER BY m.fechaHora ASC")
    List<Mensaje> findByEjemplarIdOrderByFechaHoraAsc(Long ejemplarId);
}
