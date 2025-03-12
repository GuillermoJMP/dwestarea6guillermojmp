package com.dwes.serviceImpl;

import com.dwes.models.Ejemplar;
import com.dwes.repositories.EjemplarRepository;
import com.dwes.services.EjemplarService;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class EjemplarServiceImpl implements EjemplarService {

    @Autowired
    private EjemplarRepository ejemplarRepository;

    @Override
    public List<Ejemplar> listarTodos() {
        return ejemplarRepository.findAll();
    }

    @Override
    @Transactional
    public Ejemplar guardar(Ejemplar ejemplar) {
        return ejemplarRepository.save(ejemplar);
    }

    @Override
    public Ejemplar obtenerPorId(Long id) {
        return ejemplarRepository.findById(id).orElse(null);
    }

    @Override
    public List<Ejemplar> filtrarPorPlanta(Long plantaId) {
        return ejemplarRepository.findByPlantaId(plantaId);
    }
}
