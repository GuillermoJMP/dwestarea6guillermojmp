package com.dwes.controllers;

import com.dwes.models.Credenciales;
import com.dwes.serviceImpl.CredencialesServiceImpl;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class CredencialesController {

	@Autowired
	private CredencialesServiceImpl credencialesService;

	@GetMapping("/login")
	public String mostrarLogin() {
		return "login";
	}

	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate(); // 🔹 Elimina la sesión completamente
		return "redirect:/inicio"; // 🔹 Redirige a la página de inicio como usuario no autenticado
	}

	@PostMapping("/autenticar")
	public String autenticar(@RequestParam String usuario, @RequestParam String password, HttpSession session) {
		// 🔹 Buscar al usuario en la base de datos
		Credenciales credenciales = credencialesService.obtenerUsuario(usuario);

		// 🔹 Verificar si el usuario existe y la contraseña es correcta
		if (credenciales != null && credenciales.getPassword().equals(password)) {
			// 🔹 Almacenar los datos en la sesión
			session.setAttribute("usuarioLogeado", usuario);
			session.setAttribute("rol", credenciales.getRol());

			return "redirect:/inicio"; // 🔹 Redirige a la página de inicio
		}

		return "redirect:/login?error=true"; // 🔹 Si falla, redirige al login con error
	}
}
