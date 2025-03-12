package com.dwes.controllers;

import com.dwes.models.Planta;
import com.dwes.services.PlantaService;
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

    // Listar todas las plantas ordenadas por nombre común
    @GetMapping("/plantasAdmin")
    public String listar(Model model) {
        List<Planta> plantas = plantaService.listarTodasOrdenadas();
        model.addAttribute("plantas", plantas);

        // Si no existe un objeto "planta" en el modelo, lo creamos vacío
        if (!model.containsAttribute("planta")) {
            model.addAttribute("planta", new Planta());
        }

        return "plantasAdmin";
    }

    // Guardar o actualizar una planta
    @PostMapping("/guardarPlanta")
    public String guardar(@ModelAttribute Planta planta, RedirectAttributes redirectAttributes) {
        // Validar campos obligatorios
        if (planta.getCodigo().trim().isEmpty() || planta.getNombreComun().trim().isEmpty()
                || planta.getNombreCientifico().trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Todos los campos son obligatorios.");
            return "redirect:/plantasAdmin";
        }

        // Validar que el código solo contenga letras y sin espacios
        String codigoNormalizado = planta.getCodigo().replaceAll("\\s+", "").toUpperCase();
        if (!codigoNormalizado.matches("[A-Za-z]+")) {
            redirectAttributes.addFlashAttribute("errorMessage", "El código solo puede contener letras y sin espacios.");
            return "redirect:/plantasAdmin";
        }
        planta.setCodigo(codigoNormalizado);

        // Verificar si el código ya existe (solo si es una nueva planta)
        if (planta.getId() == null && plantaService.existeCodigo(codigoNormalizado)) {
            redirectAttributes.addFlashAttribute("errorMessage", "El código ya está en uso.");
            return "redirect:/plantasAdmin";
        }

        // Verificar si el nombre común o científico ya existen en nuevas plantas
        if (planta.getId() == null) {
            if (plantaService.existeNombreComun(planta.getNombreComun())) {
                redirectAttributes.addFlashAttribute("errorMessage", "El nombre común ya está en uso.");
                return "redirect:/plantasAdmin";
            }
            if (plantaService.existeNombreCientifico(planta.getNombreCientifico())) {
                redirectAttributes.addFlashAttribute("errorMessage", "El nombre científico ya está en uso.");
                return "redirect:/plantasAdmin";
            }
        }

        // Guardar la planta en la base de datos
        plantaService.guardar(planta);
        redirectAttributes.addFlashAttribute("successMessage", "Planta guardada correctamente.");
        return "redirect:/plantasAdmin";
    }

    // Cargar datos de una planta para edición sin salir de la página
    @GetMapping("/editar/{id}")
    public String buscarPorId(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Planta> planta = plantaService.obtenerPorId(id);
        
        if (planta.isPresent()) {
            redirectAttributes.addFlashAttribute("planta", planta.get());
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Planta no encontrada.");
        }

        return "redirect:/plantasAdmin";
    }
}
