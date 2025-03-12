package com.dwes.controllers;

import com.dwes.models.Persona;
import com.dwes.services.PersonaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class PersonaController {

    @Autowired
    private PersonaService personaService;

    // 🔹 Listar todas las personas en personaAdmin.html
    @GetMapping("/personaAdmin")
    public String listar(Model model) {
        List<Persona> personas = personaService.listarTodos();
        model.addAttribute("personas", personas);
        return "personaAdmin";  // 🔹 Asegurar que carga personaAdmin.html
    }

    // 🔹 Guardar una nueva persona
    @PostMapping("/guardarPersona")
    public String guardar(@ModelAttribute Persona persona, RedirectAttributes redirectAttributes) {

        // 🔹 Validar que los campos no estén vacíos
        if (persona.getNombre().trim().isEmpty() || persona.getEmail().trim().isEmpty() ||
            persona.getUsuario().trim().isEmpty() || persona.getPassword().trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Todos los campos son obligatorios.");
            return "redirect:/personaAdmin";
        }

        // 🔹 Validar que el email y el usuario no estén en uso
        if (personaService.existeEmail(persona.getEmail())) {
            redirectAttributes.addFlashAttribute("errorMessage", "El email ya está en uso.");
            return "redirect:/personaAdmin";
        }
        if (personaService.existeUsuario(persona.getUsuario())) {
            redirectAttributes.addFlashAttribute("errorMessage", "El nombre de usuario ya está en uso.");
            return "redirect:/personaAdmin";
        }

        // 🔹 Validar que el usuario no contenga espacios y sea alfanumérico
        if (!persona.getUsuario().matches("^[a-zA-Z0-9]+$")) {
            redirectAttributes.addFlashAttribute("errorMessage", "El nombre de usuario solo puede contener letras y números.");
            return "redirect:/personaAdmin";
        }

        // 🔹 Validar que la contraseña tenga al menos 6 caracteres y no contenga espacios
        if (persona.getPassword().length() < 6 || persona.getPassword().contains(" ")) {
            redirectAttributes.addFlashAttribute("errorMessage", "La contraseña debe tener al menos 6 caracteres y no contener espacios.");
            return "redirect:/personaAdmin";
        }

        // Guardar la persona en la base de datos
        personaService.guardar(persona);
        redirectAttributes.addFlashAttribute("successMessage", "Persona registrada correctamente.");
        return "redirect:/personaAdmin";
    }
}
