package com.dwes.controllers;

import com.dwes.models.Ejemplar;
import com.dwes.models.Planta;
import com.dwes.models.Mensaje;
import com.dwes.models.Persona;
import com.dwes.services.EjemplarService;
import com.dwes.services.PlantaService;
import com.dwes.services.MensajeService;
import com.dwes.services.PersonaService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
public class EjemplarController {

	@Autowired
	private EjemplarService ejemplarService;

	@Autowired
	private PlantaService plantaService;

	@Autowired
	private MensajeService mensajeService;

	@Autowired
	private PersonaService personaService;

	@GetMapping("/ejemplaresAdmin")
	public String listar(Model model, @RequestParam(required = false) Long plantaId, HttpSession session,
			RedirectAttributes redirectAttributes) {
		String rol = (String) session.getAttribute("rol");
		if (rol == null || (!rol.equals("ADMIN") && !rol.equals("PERSONAL"))) {
			redirectAttributes.addFlashAttribute("errorMessage", "Acceso denegado. No tienes permisos.");
			return "redirect:/inicio";
		}
		List<Ejemplar> ejemplares = (plantaId == null) ? ejemplarService.listarTodos()
				: ejemplarService.filtrarPorPlanta(plantaId);
		for (Ejemplar ejemplar : ejemplares) {
			ejemplar.setNumeroMensajes(ejemplarService.contarMensajesPorEjemplar(ejemplar.getId()));
			ejemplar.setUltimoMensaje(ejemplarService.obtenerUltimaFechaMensaje(ejemplar.getId()));
		}
		model.addAttribute("plantas", plantaService.listarTodas());
		model.addAttribute("ejemplares", ejemplares);
		model.addAttribute("plantaSeleccionada", plantaId);
		return "ejemplaresAdmin";
	}

	@PostMapping("/guardarEjemplar")
	public String guardar(@RequestParam Long planta, HttpSession session, RedirectAttributes redirectAttributes) {
		String rol = (String) session.getAttribute("rol");
		if (rol == null || (!rol.equals("ADMIN") && !rol.equals("PERSONAL"))) {
			redirectAttributes.addFlashAttribute("errorMessage", "Acceso denegado. No tienes permisos.");
			return "redirect:/inicio";
		}
		Optional<Planta> optionalPlanta = plantaService.obtenerPorId(planta);
		if (optionalPlanta.isPresent()) {
			Planta selectedPlanta = optionalPlanta.get();
			Ejemplar ejemplar = new Ejemplar();
			ejemplar.setPlanta(selectedPlanta);
			ejemplar = ejemplarService.guardar(ejemplar);
			ejemplar.setNombre(selectedPlanta.getCodigo() + "_" + ejemplar.getId());
			ejemplar = ejemplarService.guardar(ejemplar);
			String usuarioLogeado = (String) session.getAttribute("usuarioLogeado");
			Persona personaLogeada = personaService.obtenerPorId((Long) session.getAttribute("usuarioId"));
			Mensaje mensajeInicial = new Mensaje();
			mensajeInicial.setEjemplar(ejemplar);
			mensajeInicial.setMensaje("Ejemplar creado por " + usuarioLogeado);
			mensajeInicial.setFechaHora(LocalDateTime.now());
			mensajeInicial.setPersona(personaLogeada);
			mensajeService.guardar(mensajeInicial);
			redirectAttributes.addFlashAttribute("successMessage", "Ejemplar registrado correctamente.");
		} else {
			redirectAttributes.addFlashAttribute("errorMessage", "Error al registrar el ejemplar.");
		}
		return "redirect:/ejemplaresAdmin";
	}

	@PostMapping("/modificarEjemplar")
	public String modificarEjemplar(@RequestParam Long ejemplarId, @RequestParam Long nuevaPlantaId,
			RedirectAttributes redirectAttributes, HttpSession session) {
		String rol = (String) session.getAttribute("rol");
		if (rol == null || (!rol.equals("ADMIN") && !rol.equals("PERSONAL"))) {
			redirectAttributes.addFlashAttribute("errorMessage", "Acceso denegado. No tienes permisos.");
			return "redirect:/inicio";
		}
		Optional<Ejemplar> ejemplarOpt = ejemplarService.obtenerPorId(ejemplarId);
		Optional<Planta> plantaOpt = plantaService.obtenerPorId(nuevaPlantaId);
		if (ejemplarOpt.isPresent() && plantaOpt.isPresent()) {
			Ejemplar ejemplar = ejemplarOpt.get();
			Planta nuevaPlanta = plantaOpt.get();
			ejemplar.setPlanta(nuevaPlanta);
			ejemplar.setNombre(nuevaPlanta.getCodigo() + "_" + ejemplar.getId());
			ejemplarService.guardar(ejemplar);
			redirectAttributes.addFlashAttribute("successMessage", "Ejemplar modificado correctamente.");
		} else {
			redirectAttributes.addFlashAttribute("errorMessage", "Error al modificar el ejemplar.");
		}
		return "redirect:/ejemplaresAdmin";
	}
}
