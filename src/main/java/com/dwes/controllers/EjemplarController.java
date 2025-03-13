package com.dwes.controllers;

import com.dwes.models.Ejemplar;
import com.dwes.models.Planta;
import com.dwes.services.EjemplarService;
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
public class EjemplarController {

    @Autowired
    private EjemplarService ejemplarService;

    @Autowired
    private PlantaService plantaService;

    @GetMapping("/ejemplaresAdmin")
    public String listar(Model model, @RequestParam(required = false) Long plantaId, HttpSession session, RedirectAttributes redirectAttributes) {
        String rol = (String) session.getAttribute("rol");

        if (rol == null || (!rol.equals("ADMIN") && !rol.equals("PERSONAL"))) {
            redirectAttributes.addFlashAttribute("errorMessage", "Acceso denegado. No tienes permisos.");
            return "redirect:/inicio";
        }

        List<Ejemplar> ejemplares = (plantaId == null) ? ejemplarService.listarTodos() : ejemplarService.filtrarPorPlanta(plantaId);
        model.addAttribute("plantas", plantaService.listarTodas());
        model.addAttribute("ejemplares", ejemplares);

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

            // Guardar ejemplar sin nombre y obtener su ID
            ejemplar = ejemplarService.guardar(ejemplar);
            ejemplar.setNombre(selectedPlanta.getCodigo() + "_" + ejemplar.getId());

            // Guardar nuevamente con el nombre generado
            ejemplarService.guardar(ejemplar);

            redirectAttributes.addFlashAttribute("successMessage", "Ejemplar registrado correctamente.");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al registrar el ejemplar.");
        }

        return "redirect:/ejemplaresAdmin";
    }
}
