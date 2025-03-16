package com.dwes.services;

import com.dwes.models.Mensaje;
import java.time.LocalDateTime;
import java.util.List;

public interface MensajeService {
	List<Mensaje> listarTodos();

	void guardar(Mensaje mensaje);

	List<Mensaje> buscarPorPersona(Long personaId);

	List<Mensaje> buscarPorEjemplar(Long ejemplarId);

	List<Mensaje> buscarPorRangoFechas(LocalDateTime inicio, LocalDateTime fin);

	List<Mensaje> obtenerMensajesPorEjemplarOrdenados(Long ejemplarId);

	List<Mensaje> buscarPorPlanta(Long plantaId);
}
