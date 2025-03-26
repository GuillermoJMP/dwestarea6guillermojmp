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
import java.util.ArrayList;
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
	public String listar(Model model, @RequestParam(required = false) List<Long> plantaIds, HttpSession session,
			RedirectAttributes redirectAttributes) {
		String rol = (String) session.getAttribute("rol");
		if (rol == null || (!rol.equals("ADMIN") && !rol.equals("PERSONAL"))) {
			redirectAttributes.addFlashAttribute("errorMessage", "Acceso denegado. No tienes permisos.");
			return "redirect:/inicio";
		}
		List<Planta> todasPlantas = plantaService.listarTodas();

		List<Planta> plantasConEjemplares = new ArrayList<>();
		for (Planta p : todasPlantas) {
			int count = ejemplarService.filtrarPorPlanta(p.getId()).size();
			if (count > 0) {
				plantasConEjemplares.add(p);
			}
		}

		List<Ejemplar> ejemplares;
		if (plantaIds == null || plantaIds.isEmpty()) {
			ejemplares = ejemplarService.listarTodos();
		} else {
			ejemplares = ejemplarService.filtrarPorPlantas(plantaIds);
		}

		for (Ejemplar e : ejemplares) {
			e.setNumeroMensajes(ejemplarService.contarMensajesPorEjemplar(e.getId()));
			e.setUltimoMensaje(ejemplarService.obtenerUltimaFechaMensaje(e.getId()));
		}

		model.addAttribute("plantasFiltrables", plantasConEjemplares);
		model.addAttribute("plantas", todasPlantas);

		model.addAttribute("ejemplares", ejemplares);
		model.addAttribute("plantaSeleccionadas", plantaIds);
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
			int count = ejemplarService.contarEjemplaresPorPlanta(planta);
			ejemplar.setNombre(selectedPlanta.getCodigo() + "_" + count);
			ejemplar = ejemplarService.guardar(ejemplar);
			String usuarioLogeado = (String) session.getAttribute("usuarioLogeado");
			Persona personaLogeada = personaService.obtenerPorId((Long) session.getAttribute("usuarioId"));
			Mensaje mensajeInicial = new Mensaje();
			mensajeInicial.setEjemplar(ejemplar);
			mensajeInicial.setMensaje("Ejemplar " + ejemplar.getNombre() + " creado por " + usuarioLogeado);
			mensajeInicial.setFechaHora(LocalDateTime.now());
			mensajeInicial.setPersona(personaLogeada);  
			mensajeService.guardar(mensajeInicial);


			redirectAttributes.addFlashAttribute("successMessage", "Ejemplar registrado correctamente.");
		} else {
			redirectAttributes.addFlashAttribute("errorMessage", "Error al registrar el ejemplar.");
		}
		return "redirect:/ejemplaresAdmin";
	}

}
