package com.dwes.serviceImpl;

import com.dwes.models.Ejemplar;
import com.dwes.models.Mensaje;
import com.dwes.repositories.EjemplarRepository;
import com.dwes.repositories.MensajeRepository;
import com.dwes.services.EjemplarService;
import com.dwes.services.MensajeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class EjemplarServiceImpl implements EjemplarService {

    @Autowired
    private EjemplarRepository ejemplarRepository;

    @Autowired
    private MensajeService mensajeService;

    @Autowired
    private MensajeRepository mensajeRepository;

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
    
    @Override
    public List<Ejemplar> filtrarPorPlantas(List<Long> plantaIds) {
        return ejemplarRepository.findByPlantaIdIn(plantaIds);
    }

    @Override
    public int contarMensajesPorEjemplar(Long ejemplarId) {
        return mensajeService.buscarPorEjemplar(ejemplarId).size();
    }

    @Override
    public LocalDateTime obtenerUltimaFechaMensaje(Long ejemplarId) {
        return mensajeRepository.findTopByEjemplarIdOrderByFechaHoraDesc(ejemplarId)
                .map(Mensaje::getFechaHora)
                .orElse(null);
    }
    
    @Override
    public int contarEjemplaresPorPlanta(Long plantaId) {
        return ejemplarRepository.countByPlantaId(plantaId);
    }

}
