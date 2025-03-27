package com.dwes.serviceImpl;

import com.dwes.models.Credenciales;
import com.dwes.models.Persona;
import com.dwes.repositories.CredencialesRepository;
import com.dwes.repositories.PersonaRepository;
import com.dwes.services.CredencialesService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class CredencialesServiceImpl implements CredencialesService {

	@Autowired
	private CredencialesRepository credencialesRepository;

	@Autowired
	private PersonaRepository personaRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public boolean autenticar(String usuario, String password) {
		Optional<Credenciales> credencialesOpt = Optional.ofNullable(credencialesRepository.findByUsuario(usuario));
		return credencialesOpt.isPresent() && passwordEncoder.matches(password, credencialesOpt.get().getPassword());
	}

    @Override
    public void guardar(Credenciales credenciales) {
        credenciales.setPassword(passwordEncoder.encode(credenciales.getPassword()));
        credencialesRepository.save(credenciales);
    }

	@Override
	public Optional<Credenciales> obtenerUsuario(String usuario) {
		return Optional.ofNullable(credencialesRepository.findByUsuario(usuario));
	}

	@PostConstruct
	public void crearAdminSiNoExiste() {
		if (credencialesRepository.findByUsuario("admin") == null) {
			Credenciales adminCredenciales = new Credenciales("admin", passwordEncoder.encode("admin"), "ADMIN");
			credencialesRepository.save(adminCredenciales);
		}
		if (personaRepository.findByUsuario("admin") == null) {
			Persona adminPersona = new Persona();
			adminPersona.setNombre("Administrador");
			adminPersona.setEmail("admin@admin.com");
			adminPersona.setUsuario("admin");
			adminPersona.setPassword(passwordEncoder.encode("admin"));
			personaRepository.save(adminPersona);
		}
	}

}
