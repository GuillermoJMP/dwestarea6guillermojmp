package com.dwes.serviceImpl;

import com.dwes.models.Mensaje;
import com.dwes.repositories.MensajeRepository;
import com.dwes.services.MensajeService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class MensajeServiceImpl implements MensajeService {

    private final MensajeRepository mensajeRepository;

    public MensajeServiceImpl(MensajeRepository mensajeRepository) {
        this.mensajeRepository = mensajeRepository;
    }

    @Override
    public List<Mensaje> listarTodos() {
        return mensajeRepository.findAll();
    }

    @Override
    public void guardar(Mensaje mensaje) {
        mensajeRepository.save(mensaje);
    }

    @Override
    public List<Mensaje> buscarPorPersona(Long personaId) {
        return mensajeRepository.findByPersonaId(personaId);
    }

    @Override
    public List<Mensaje> buscarPorEjemplar(Long ejemplarId) {
        return mensajeRepository.findByEjemplarId(ejemplarId);
    }

    @Override
    public List<Mensaje> buscarPorRangoFechas(LocalDateTime inicio, LocalDateTime fin) {
        return mensajeRepository.findByFechaHoraBetween(inicio, fin);
    }
}
