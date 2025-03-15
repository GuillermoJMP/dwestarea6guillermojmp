package com.dwes.serviceImpl;

import com.dwes.models.Planta;
import com.dwes.repositories.PlantaRepository;
import com.dwes.services.PlantaService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class PlantaServiceImpl implements PlantaService {

	@Autowired
	private PlantaRepository plantaRepository;

	@Override
	public List<Planta> listarTodas() {
		return plantaRepository.findAll();
	}

	@Override
	public List<Planta> listarTodasOrdenadas() {
		return plantaRepository.findAllByOrderByNombreComunAsc();
	}

	@Override
	@Transactional
	public Planta guardar(Planta planta) {
		return plantaRepository.save(planta);
	}

	@Override
	public Optional<Planta> obtenerPorId(Long id) {
		return plantaRepository.findById(id);
	}

	@Override
	public boolean existeCodigo(String codigo) {
		return plantaRepository.existsByCodigo(codigo);
	}

	@Override
	public boolean existeNombreComun(String nombreComun) {
		return plantaRepository.existsByNombreComun(nombreComun);
	}

	@Override
	public boolean existeNombreCientifico(String nombreCientifico) {
		return plantaRepository.existsByNombreCientifico(nombreCientifico);
	}
}
