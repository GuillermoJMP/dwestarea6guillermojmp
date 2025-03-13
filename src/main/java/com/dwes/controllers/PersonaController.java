package com.dwes.controllers;

import com.dwes.models.Persona;
import com.dwes.models.Credenciales;
import com.dwes.services.PersonaService;
import com.dwes.services.CredencialesService;
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

    @Autowired
    private CredencialesService credencialesService;  // 🔹 Inyectamos el servicio de credenciales

    @GetMapping("/personaAdmin")
    public String listar(Model model) {
        List<Persona> personas = personaService.listarTodos();
        model.addAttribute("personas", personas);
        return "personaAdmin";
    }

    @PostMapping("/guardarPersona")
    public String guardar(@ModelAttribute Persona persona, RedirectAttributes redirectAttributes) {

        // 🔹 Validaciones
        if (persona.getNombre().trim().isEmpty() || persona.getEmail().trim().isEmpty() ||
            persona.getUsuario().trim().isEmpty() || persona.getPassword().trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Todos los campos son obligatorios.");
            return "redirect:/personaAdmin";
        }

        if (personaService.existeEmail(persona.getEmail())) {
            redirectAttributes.addFlashAttribute("errorMessage", "El email ya está en uso.");
            return "redirect:/personaAdmin";
        }

        if (personaService.existeUsuario(persona.getUsuario())) {
            redirectAttributes.addFlashAttribute("errorMessage", "El nombre de usuario ya está en uso.");
            return "redirect:/personaAdmin";
        }

        // 🔹 Guardar persona en la base de datos
        personaService.guardar(persona);

        // 🔹 Guardar también las credenciales del usuario
        Credenciales credenciales = new Credenciales(
            persona.getUsuario(),
            persona.getPassword(),
            "PERSONAL"  // 🔹 Asignamos el rol por defecto para nuevos usuarios
        );
        credencialesService.guardar(credenciales);

        redirectAttributes.addFlashAttribute("successMessage", "Persona registrada correctamente.");
        return "redirect:/personaAdmin";
    }
}
