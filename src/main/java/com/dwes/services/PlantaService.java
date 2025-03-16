package com.dwes.services;

import com.dwes.models.Planta;
import java.util.List;
import java.util.Optional;

public interface PlantaService {
	List<Planta> listarTodas();

	List<Planta> listarTodasOrdenadas();

	Planta guardar(Planta planta);

	Optional<Planta> obtenerPorId(Long id);

	boolean existeCodigo(String codigo);

	boolean existeNombreComun(String nombreComun);

	boolean existeNombreCientifico(String nombreCientifico);
}
