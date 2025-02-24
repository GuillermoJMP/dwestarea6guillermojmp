package com.dwes.controllers;

import com.dwes.models.Persona;
import com.dwes.services.PersonaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class PersonaController {

    @Autowired
    private PersonaService personaService;

    // Listar todas las personas
    @GetMapping("/persona")
    public String listar(Model model) {
        List<Persona> personas = personaService.listarTodos();
        model.addAttribute("personas", personas);
        return "persona";
    }

    // Guardar una nueva persona
    @PostMapping("/guardarPersona")
    public String guardar(@RequestParam String nombre, @RequestParam String email, Model model) {
        Persona persona = new Persona();
        persona.setNombre(nombre);
        persona.setEmail(email);

        personaService.guardar(persona);

        model.addAttribute("personas", personaService.listarTodos());
        return "persona";
    }
}
