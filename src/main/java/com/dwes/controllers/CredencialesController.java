package com.dwes.controllers;

import com.dwes.serviceImpl.CredencialesServiceImpl;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class CredencialesController {

	@Autowired
	private CredencialesServiceImpl credencialesService;

	// Muestra la página de login
	@GetMapping("/login")
	public String mostrarLogin() {
		return "login";
	}

	// Cierra sesión y redirige a login
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate(); // Elimina la sesión
		return "redirect:/inicio"; // Redirige a inicio como usuario no logeado
	}

	// Autenticar usuario
	@PostMapping("/autenticar")
	public String autenticar(@RequestParam String usuario, @RequestParam String password, HttpSession session) {
		if (credencialesService.autenticar(usuario, password)) {
			session.setAttribute("usuarioLogeado", usuario); // Guarda el usuario en la sesión
			return "redirect:/inicio";
		}
		return "redirect:/login?error=true";
	}
}
