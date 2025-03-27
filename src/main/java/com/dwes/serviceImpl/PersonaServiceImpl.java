package com.dwes.serviceImpl;

import com.dwes.models.Persona;
import com.dwes.repositories.PersonaRepository;
import com.dwes.services.PersonaService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PersonaServiceImpl implements PersonaService {

    @Autowired
    private PersonaRepository personaRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public List<Persona> listarTodos() {
        return personaRepository.findAll();
    }

    @Override
    @Transactional
    public Persona guardar(Persona persona) {
        persona.setPassword(passwordEncoder.encode(persona.getPassword()));
        return personaRepository.save(persona);
    }

    @Override
    public void eliminar(Long id) {
        personaRepository.deleteById(id);
    }

    @Override
    public Persona obtenerPorId(Long id) {
        return personaRepository.findById(id).orElse(null);
    }

    @Override
    public boolean existeEmail(String email) {
        return personaRepository.existsByEmail(email);
    }

    @Override
    public boolean existeUsuario(String usuario) {
        return personaRepository.existsByUsuario(usuario);
    }

    @Override
    public Persona obtenerPorUsuario(String usuario) {
        return personaRepository.findByUsuario(usuario);
    }
}
