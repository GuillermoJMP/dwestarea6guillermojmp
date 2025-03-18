package com.dwes.controllers;

import com.dwes.models.Planta;
import com.dwes.services.PlantaService;
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

	@GetMapping("/inicio")
	public String mostrarInicio(Model model, HttpSession session) {
		List<Planta> plantas = plantaService.listarTodas();
		model.addAttribute("plantas", plantas);

		String usuarioLogeado = (String) session.getAttribute("usuarioLogeado");
		String rolUsuario = (String) session.getAttribute("rol");

		if (usuarioLogeado == null) {
			model.addAttribute("nombreUsuario", "Invitado");
			model.addAttribute("mensaje",
					"Bienvenido a Viveros Acme. Inicia sesión para acceder a más funcionalidades.");
			model.addAttribute("rol", "INVITADO");
		} else {
			model.addAttribute("nombreUsuario", usuarioLogeado);
			model.addAttribute("rol", rolUsuario);

			switch (rolUsuario) {
			case "ADMIN":
				model.addAttribute("mensaje", "Bienvenido, Administrador. Tienes acceso total al sistema.");
				break;
			case "PERSONAL":
				model.addAttribute("mensaje",
						"Bienvenido, " + usuarioLogeado + ". Puedes registrar y gestionar ejemplares.");
				break;
			default:
				model.addAttribute("mensaje", "Bienvenido, " + usuarioLogeado + "!");
				break;
			}
		}

		return "inicio";
	}
}
