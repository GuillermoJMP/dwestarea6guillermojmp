package com.dwes.services;

import com.dwes.models.Ejemplar;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EjemplarService {
	List<Ejemplar> listarTodos();

	Ejemplar guardar(Ejemplar ejemplar);

	Optional<Ejemplar> obtenerPorId(Long id);

	List<Ejemplar> filtrarPorPlanta(Long plantaId);

	int contarMensajesPorEjemplar(Long ejemplarId);

	LocalDateTime obtenerUltimaFechaMensaje(Long ejemplarId);
}
