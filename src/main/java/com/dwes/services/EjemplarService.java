package com.dwes.services;

import com.dwes.models.Ejemplar;
import java.util.List;

public interface EjemplarService {
    List<Ejemplar> listarTodos();
    Ejemplar guardar(Ejemplar ejemplar);
    Ejemplar obtenerPorId(Long id);
    List<Ejemplar> filtrarPorPlanta(Long plantaId);
}
