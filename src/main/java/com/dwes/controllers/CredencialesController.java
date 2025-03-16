package com.dwes.controllers;

import com.dwes.models.Credenciales;
import com.dwes.models.Persona;
import com.dwes.services.CredencialesService;
import com.dwes.services.PersonaService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class CredencialesController {

	@Autowired
	private CredencialesService credencialesService;

	@Autowired
	private PersonaService personaService;

	@GetMapping("/login")
	public String mostrarLogin() {
		return "login";
	}

	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/inicio";
	}

	@PostMapping("/autenticar")
	public String autenticar(@RequestParam String usuario, @RequestParam String password, HttpSession session) {
		if (usuario == null || password == null || usuario.trim().isEmpty() || password.trim().isEmpty()) {
			return "redirect:/login?error=camposVacios";
		}
		Credenciales credenciales = credencialesService.obtenerUsuario(usuario);
		if (credenciales == null || !credenciales.getPassword().equals(password)) {
			return "redirect:/login?error=credencialesInvalidas";
		}
		session.setAttribute("usuarioLogeado", usuario);
		session.setAttribute("rol", credenciales.getRol());
		// Asumimos que PersonaService tiene el método obtenerPorUsuario
		Persona persona = personaService.obtenerPorUsuario(usuario);
		session.setAttribute("usuarioId", persona.getId());
		if (credenciales.getRol().equals("CLIENTE")) {
			return "redirect:/zonaCliente";
		}
		return "redirect:/inicio";
	}
}
