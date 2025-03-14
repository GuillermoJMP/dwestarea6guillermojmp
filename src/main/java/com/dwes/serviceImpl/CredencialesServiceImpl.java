package com.dwes.serviceImpl;

import com.dwes.models.Credenciales;
import com.dwes.repositories.CredencialesRepository;
import com.dwes.services.CredencialesService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CredencialesServiceImpl implements CredencialesService {

	@Autowired
	private CredencialesRepository credencialesRepository;

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
	public Credenciales obtenerUsuario(String usuario) { // 🔹 Método para obtener usuario
		return credencialesRepository.findByUsuario(usuario);
	}

	// 🔹 Método que se ejecuta al iniciar la aplicación
	@PostConstruct
	public void crearAdminSiNoExiste() {
		if (credencialesRepository.findByUsuario("admin") == null) {
			Credenciales admin = new Credenciales("admin", "admin", "ADMIN");
			credencialesRepository.save(admin);
			System.out.println("🛠️ Usuario admin creado automáticamente.");
		}
	}
}
