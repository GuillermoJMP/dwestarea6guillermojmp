package com.dwes.serviceImpl;

import com.dwes.models.Planta;
import com.dwes.repositories.PlantaRepository;
import com.dwes.services.PlantaService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import com.dwes.models.Persona;
import com.dwes.services.PersonaService;
import java.util.ArrayList;
import java.util.Comparator;

@Service
public class PlantaServiceImpl implements PlantaService {

	@Autowired
	private PlantaRepository plantaRepository;

	@Autowired
	private PersonaService personaService;

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

	@Override
	public void agregarAFavoritos(String usuario, Long plantaId) {
		Persona persona = personaService.obtenerPorUsuario(usuario);
		if (persona == null) {
			throw new RuntimeException("Usuario no encontrado");
		}

		Planta planta = plantaRepository.findById(plantaId)
				.orElseThrow(() -> new RuntimeException("Planta no encontrada"));

		persona.getFavoritos().add(planta);
		personaService.actualizar(persona);
	}

	@Override
	public void quitarDeFavoritos(String usuario, Long plantaId) {
		Persona persona = personaService.obtenerPorUsuario(usuario);
		if (persona == null) {
			throw new RuntimeException("Usuario no encontrado");
		}

		persona.getFavoritos().removeIf(planta -> planta.getId().equals(plantaId));
		personaService.actualizar(persona);
	}

	@Override
	public boolean esFavorito(String usuario, Long plantaId) {
		Persona persona = personaService.obtenerPorUsuario(usuario);
		return persona != null && persona.getFavoritos().stream()
				.anyMatch(planta -> planta.getId().equals(plantaId));
	}

	@Override
	public List<Planta> ordenarFavoritosPrimero(List<Planta> plantas, String usuario) {
		List<Planta> listaOrdenada = new ArrayList<>(plantas);
		Persona persona = personaService.obtenerPorUsuario(usuario);

		if (persona != null) {
			listaOrdenada.sort(Comparator.comparing(
				planta -> !persona.getFavoritos().stream()
						.anyMatch(fav -> fav.getId().equals(planta.getId()))
			));
		}

		return listaOrdenada;
	}
}
