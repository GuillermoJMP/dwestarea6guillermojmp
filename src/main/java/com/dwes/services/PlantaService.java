package com.dwes.services;

import com.dwes.models.Planta;
import java.util.List;

public interface PlantaService {
    List<Planta> listarTodas();
    Planta guardar(Planta planta);
    Planta obtenerPorId(Long id);
}
