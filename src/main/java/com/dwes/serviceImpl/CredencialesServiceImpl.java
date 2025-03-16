package com.dwes.serviceImpl;

import com.dwes.models.Credenciales;
import com.dwes.models.Persona;
import com.dwes.repositories.CredencialesRepository;
import com.dwes.repositories.PersonaRepository;
import com.dwes.services.CredencialesService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CredencialesServiceImpl implements CredencialesService {

	@Autowired
	private CredencialesRepository credencialesRepository;

	@Autowired
	private PersonaRepository personaRepository;

	@Override
	public boolean autenticar(String usuario, String password) {
		Credenciales credenciales = credencialesRepository.findByUsuario(usuario);
		return credenciales != null && credenciales.getPassword().equals(password);
	}

	@Override
	public void guardar(Credenciales credenciales) {
		credencialesRepository.save(credenciales);
	}

	@Override
	public Credenciales obtenerUsuario(String usuario) {
		return credencialesRepository.findByUsuario(usuario);
	}

	@PostConstruct
	public void crearAdminSiNoExiste() {
		if (credencialesRepository.findByUsuario("admin") == null) {
			credencialesRepository.save(new Credenciales("admin", "admin", "ADMIN"));
		}
		if (personaRepository.findByUsuario("admin") == null) {
			Persona adminPersona = new Persona();
			adminPersona.setNombre("Administrador");
			adminPersona.setEmail("admin@tuapp.com");
			adminPersona.setUsuario("admin");
			adminPersona.setPassword("admin");
			personaRepository.save(adminPersona);
		}
	}
}
