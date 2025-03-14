package com.dwes.serviceImpl;

import com.dwes.models.Ejemplar;
import com.dwes.repositories.EjemplarRepository;
import com.dwes.services.EjemplarService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EjemplarServiceImpl implements EjemplarService {

    private final EjemplarRepository ejemplarRepository;

    public EjemplarServiceImpl(EjemplarRepository ejemplarRepository) {
        this.ejemplarRepository = ejemplarRepository;
    }

    @Override
    public List<Ejemplar> listarTodos() {
        return ejemplarRepository.findAll();
    }

    @Override
    public Ejemplar guardar(Ejemplar ejemplar) {
        return ejemplarRepository.save(ejemplar);
    }

    @Override
    public Optional<Ejemplar> obtenerPorId(Long id) {
        return ejemplarRepository.findById(id);
    }

    @Override
    public List<Ejemplar> filtrarPorPlanta(Long plantaId) {
        return ejemplarRepository.findByPlantaId(plantaId);
    }
}
