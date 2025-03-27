package com.dwes.controllers;

import com.dwes.models.Persona;
import com.dwes.models.Credenciales;
import com.dwes.services.PersonaService;
import com.dwes.services.CredencialesService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ClienteController {

	@Autowired
	private PersonaService personaService;

	@Autowired
	private CredencialesService credencialesService;

	@GetMapping("/registroCliente")
	public String mostrarRegistroCliente(Model model) {
		if (!model.containsAttribute("cliente")) {
			model.addAttribute("cliente", new Persona());
		}
		return "registroCliente";
	}

	@PostMapping("/guardarCliente")
	public String guardarCliente(@Valid @ModelAttribute("cliente") Persona cliente, BindingResult bindingResult,
			Model model, HttpSession session) {
		if (bindingResult.hasErrors()) {
			model.addAttribute("errorMessage", "Corrige los errores en el formulario.");
			return "registroCliente";
		}
		if (cliente.getNombre().trim().isEmpty() || cliente.getEmail().trim().isEmpty()
				|| cliente.getUsuario().trim().isEmpty() || cliente.getPassword().trim().isEmpty()
				|| cliente.getFechaNacimiento() == null || cliente.getNifNie().trim().isEmpty()
				|| cliente.getDireccionEnvio().trim().isEmpty() || cliente.getTelefono().trim().isEmpty()) {
			model.addAttribute("errorMessage", "Todos los campos son obligatorios.");
			return "registroCliente";
		}
		if (personaService.existeEmail(cliente.getEmail())) {
			model.addAttribute("errorMessage", "El email ya está en uso.");
			return "registroCliente";
		}
		if (personaService.existeUsuario(cliente.getUsuario())) {
			model.addAttribute("errorMessage", "El nombre de usuario ya está en uso.");
			return "registroCliente";
		}
		if (cliente.getPassword().length() < 6 || cliente.getPassword().contains(" ")) {
			model.addAttribute("errorMessage",
					"La contraseña debe tener al menos 6 caracteres y no contener espacios.");
			return "registroCliente";
		}
		String originalPassword = cliente.getPassword();
		cliente = personaService.guardar(cliente);
		Credenciales cred = new Credenciales(cliente.getUsuario(), originalPassword, "CLIENTE");
		credencialesService.guardar(cred);
		model.addAttribute("successMessage", "Cliente registrado correctamente. Por favor, inicie sesión.");
		model.addAttribute("cliente", new Persona());
		return "registroCliente";
	}

	@GetMapping("/zonaCliente")
	public String mostrarZonaCliente(HttpSession session, Model model) {
		String rol = (String) session.getAttribute("rol");
		if (rol == null || !rol.equals("CLIENTE")) {
			return "redirect:/login?error=noAutorizado";
		}
		model.addAttribute("mensaje", "Bienvenido a la zona cliente.");
		return "zonaCliente";
	}
}
