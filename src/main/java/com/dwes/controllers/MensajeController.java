package com.dwes.controllers;

import com.dwes.models.Ejemplar;
import com.dwes.models.Mensaje;
import com.dwes.models.Persona;
import com.dwes.services.EjemplarService;
import com.dwes.services.MensajeService;
import com.dwes.services.PersonaService;
import com.dwes.services.PlantaService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class MensajeController {

	@Autowired
	private MensajeService mensajeService;

	@Autowired
	private PersonaService personaService;

	@Autowired
	private EjemplarService ejemplarService;

	@Autowired
	private PlantaService plantaService;

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Long.class, "ejemplar.id", new CustomNumberEditor(Long.class, true));
	}

	@GetMapping("/mensajesAdmin")
	public String listar(@RequestParam(required = false) Long personaId, @RequestParam(required = false) String inicio,
			@RequestParam(required = false) String fin, @RequestParam(required = false) Long plantaFiltro, Model model,
			HttpSession session, RedirectAttributes redirectAttributes) {
		String rol = (String) session.getAttribute("rol");
		if (rol == null || (!rol.equals("ADMIN") && !rol.equals("PERSONAL"))) {
			redirectAttributes.addFlashAttribute("errorMessage", "Acceso denegado. No tienes permisos.");
			return "redirect:/inicio";
		}
		List<Mensaje> mensajes;
		try {
			if (plantaFiltro != null) {
				mensajes = mensajeService.buscarPorPlanta(plantaFiltro);
			} else if (personaId != null) {
				mensajes = mensajeService.buscarPorPersona(personaId);
			} else if (inicio != null && fin != null && !inicio.isEmpty() && !fin.isEmpty()) {
				LocalDateTime fechaInicio = LocalDate.parse(inicio).atStartOfDay();
				LocalDateTime fechaFin = LocalDate.parse(fin).atTime(23, 59, 59);
				mensajes = mensajeService.buscarPorRangoFechas(fechaInicio, fechaFin);
			} else {
				mensajes = mensajeService.listarTodos();
			}
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("errorMessage", "Error al filtrar mensajes.");
			return "redirect:/mensajesAdmin";
		}
		List<Ejemplar> ejemplaresConMensajes = ejemplarService.listarTodos().stream()
				.filter(e -> mensajeService.buscarPorEjemplar(e.getId()).size() > 0).collect(Collectors.toList());
		if (!model.containsAttribute("mensaje")) {
			model.addAttribute("mensaje", new Mensaje());
		}
		model.addAttribute("mensajes", mensajes);
		model.addAttribute("ejemplares", ejemplaresConMensajes);
		model.addAttribute("personas", personaService.listarTodos());
		model.addAttribute("plantas", plantaService.listarTodas());
		model.addAttribute("plantaFiltro", plantaFiltro);
		return "mensajesAdmin";
	}

	@PostMapping("/guardarMensaje")
	public String guardar(@Valid @ModelAttribute("mensaje") Mensaje mensaje, BindingResult bindingResult,
			HttpSession session, RedirectAttributes redirectAttributes, Model model) {
		String rol = (String) session.getAttribute("rol");
		if (rol == null || (!rol.equals("ADMIN") && !rol.equals("PERSONAL"))) {
			redirectAttributes.addFlashAttribute("errorMessage", "Acceso denegado. No tienes permisos.");
			return "redirect:/inicio";
		}
		if (bindingResult.hasErrors()) {
			model.addAttribute("errorMessage", "Verifica los campos.");
			model.addAttribute("mensajes", mensajeService.listarTodos());
			model.addAttribute("ejemplares", ejemplarService.listarTodos().stream()
					.filter(e -> mensajeService.buscarPorEjemplar(e.getId()).size() > 0).collect(Collectors.toList()));
			model.addAttribute("personas", personaService.listarTodos());
			model.addAttribute("plantas", plantaService.listarTodas());
			return "mensajesAdmin";
		}
		Long usuarioId = (Long) session.getAttribute("usuarioId");
		if (usuarioId == null) {
			redirectAttributes.addFlashAttribute("errorMessage", "Usuario no válido en la sesión.");
			return "redirect:/mensajesAdmin";
		}
		Persona persona = personaService.obtenerPorId(usuarioId);
		if (persona == null) {
			redirectAttributes.addFlashAttribute("errorMessage", "Persona no encontrada.");
			return "redirect:/mensajesAdmin";
		}
		mensaje.setPersona(persona);
		mensaje.setFechaHora(LocalDateTime.now());
		mensajeService.guardar(mensaje);
		redirectAttributes.addFlashAttribute("successMessage", "Mensaje guardado correctamente.");
		return "redirect:/mensajesAdmin";
	}
}
