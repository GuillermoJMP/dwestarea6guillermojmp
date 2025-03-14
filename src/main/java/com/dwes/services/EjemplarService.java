package com.dwes.services;

import com.dwes.models.Ejemplar;
import java.util.List;
import java.util.Optional;

public interface EjemplarService {
    List<Ejemplar> listarTodos();
    Ejemplar guardar(Ejemplar ejemplar);
    Optional<Ejemplar> obtenerPorId(Long id);
    List<Ejemplar> filtrarPorPlanta(Long plantaId);
    
    // 🔹 Nuevo método para contar mensajes de un ejemplar
    int contarMensajesPorEjemplar(Long ejemplarId);
    
    // 🔹 Nuevo método para obtener la fecha del último mensaje
    String obtenerUltimaFechaMensaje(Long ejemplarId);
}
