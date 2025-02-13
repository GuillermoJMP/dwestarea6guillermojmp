package com.dwes.serviceImpl;

import com.dwes.models.Planta;
import com.dwes.repositories.PlantaRepository;
import com.dwes.services.PlantaService;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlantaServiceImpl implements PlantaService {

	@Autowired
    private PlantaRepository plantaRepository;

    @Override
    public List<Planta> listarTodas() {
        return plantaRepository.findAll();
    }

    @Override
    @Transactional
    public Planta guardar(Planta planta) {
        return plantaRepository.save(planta);
    }

    @Override
    public Planta obtenerPorId(Long id) {
        return plantaRepository.findById(id).orElse(null);
    }
}
