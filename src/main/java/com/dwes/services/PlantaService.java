package com.dwes.services;

import com.dwes.models.Planta;
import java.util.List;
import java.util.Optional;

public interface PlantaService {
    List<Planta> listarTodas();
    List<Planta> listarTodasOrdenadas(); // Nuevo método para ordenar por nombre común
    Planta guardar(Planta planta);
    Optional<Planta> obtenerPorId(Long id); // Cambio para devolver Optional
    boolean existeCodigo(String codigo); // Nuevo método para verificar código único
    boolean existeNombreComun(String nombreComun); // Verifica nombre común
    boolean existeNombreCientifico(String nombreCientifico); // Verifica nombre científico
}
