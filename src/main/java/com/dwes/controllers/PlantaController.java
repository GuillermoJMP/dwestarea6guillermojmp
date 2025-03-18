package com.dwes.controllers;

import com.dwes.models.Planta;
import com.dwes.services.PlantaService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;
import java.util.Optional;

@Controller
public class PlantaController {

	@Autowired
	private PlantaService plantaService;

	@GetMapping("/plantasAdmin")
	public String listar(Model model, HttpSession session, RedirectAttributes redirectAttributes) {
		if (!esAdministrador(session)) {
			redirectAttributes.addFlashAttribute("errorMessage", "Acceso denegado. No tienes permisos.");
			return "redirect:/inicio";
		}
		List<Planta> plantas = plantaService.listarTodasOrdenadas();
		model.addAttribute("plantas", plantas);
		model.addAttribute("planta", new Planta());
		return "plantasAdmin";
	}

	@PostMapping("/guardarPlanta")
	public String guardar(@ModelAttribute Planta planta, RedirectAttributes redirectAttributes, HttpSession session) {
		if (!esAdministrador(session)) {
			redirectAttributes.addFlashAttribute("errorMessage", "Acceso denegado. No tienes permisos.");
			return "redirect:/inicio";
		}
		if (planta.getId() != null) {
			Optional<Planta> plantaExistenteOpt = plantaService.obtenerPorId(planta.getId());
			if (plantaExistenteOpt.isEmpty()) {
				redirectAttributes.addFlashAttribute("errorMessage", "La planta que intentas editar no existe.");
				return "redirect:/plantasAdmin";
			}
			planta.setCodigo(plantaExistenteOpt.get().getCodigo());
		} else {
			if (planta.getCodigo().trim().isEmpty() || planta.getNombreComun().trim().isEmpty()
					|| planta.getNombreCientifico().trim().isEmpty()) {
				redirectAttributes.addFlashAttribute("errorMessage", "Todos los campos son obligatorios.");
				return "redirect:/plantasAdmin";
			}
			String codigoNormalizado = planta.getCodigo().replaceAll("\\s+", "").toUpperCase();
			planta.setCodigo(codigoNormalizado);
			if (plantaService.existeCodigo(codigoNormalizado)) {
				redirectAttributes.addFlashAttribute("errorMessage", "El código ya está en uso.");
				return "redirect:/plantasAdmin";
			}
		}
		plantaService.guardar(planta);
		redirectAttributes.addFlashAttribute("successMessage", "Planta guardada correctamente.");
		return "redirect:/plantasAdmin";
	}

	private boolean esAdministrador(HttpSession session) {
		String rol = (String) session.getAttribute("rol");
		return rol != null && rol.equals("ADMIN");
	}
}
