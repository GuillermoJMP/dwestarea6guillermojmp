package com.dwes.controllers;

import com.dwes.models.Planta;
import com.dwes.models.Ejemplar;
import com.dwes.services.PlantaService;
import com.dwes.services.EjemplarService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class InicioController {

	@Autowired
	private PlantaService plantaService;

	@Autowired
	private EjemplarService ejemplarService; // 🔹 Inyección corregida

	// Página de inicio
	@GetMapping("/inicio")
	public String mostrarInicio(Model model, HttpSession session) {
		// Obtener la lista de plantas y ejemplares
		List<Planta> plantas = plantaService.listarTodas();
		List<Ejemplar> ejemplares = ejemplarService.listarTodos();

		model.addAttribute("plantas", plantas);
		model.addAttribute("ejemplares", ejemplares);

		// 🔹 Obtener datos de sesión
		String usuarioLogeado = (String) session.getAttribute("usuarioLogeado");
		String rolUsuario = (String) session.getAttribute("rol"); // 🔹 Obtener el rol del usuario

		// 🔹 Definir mensaje y permisos según el usuario
		if (usuarioLogeado == null) {
			model.addAttribute("nombreUsuario", "Invitado");
			model.addAttribute("mensaje",
					"Bienvenido a Viveros Acme. Inicia sesión para acceder a más funcionalidades.");
			model.addAttribute("rol", "INVITADO"); // 🔹 Definir el rol como INVITADO
		} else {
			model.addAttribute("nombreUsuario", usuarioLogeado);

			if ("ADMIN".equalsIgnoreCase(rolUsuario)) {
				model.addAttribute("mensaje", "Bienvenido, Administrador. Tienes acceso total al sistema.");
			} else if ("PERSONAL".equalsIgnoreCase(rolUsuario)) {
				model.addAttribute("mensaje",
						"Bienvenido, " + usuarioLogeado + ". Puedes registrar y gestionar ejemplares.");
			} else {
				model.addAttribute("mensaje", "Bienvenido, " + usuarioLogeado + "!");
			}

			model.addAttribute("rol", rolUsuario);
		}

		return "inicio";
	}
}
