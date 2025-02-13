package com.dwes.controllers;

import com.dwes.models.Credenciales;
import com.dwes.models.Persona;
import com.dwes.services.CredencialesService;
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
    @Autowired
    private CredencialesService credencialesService;

    @GetMapping("/persona")
    public String listar(Model model) {
        List<Persona> personas = personaService.listarTodos();
        model.addAttribute("personas", personas);
        return "persona";
    }

    @GetMapping("/nuevoPersona")
    public String crearFormulario(Model model) {
        model.addAttribute("persona", new Persona());
        return "persona";
    }

    @PostMapping("/guardarPersona")
    public String guardar(@ModelAttribute Persona persona) {
        Persona nuevaPersona = personaService.guardar(persona);

        // Crea credenciales automáticamente
        Credenciales credenciales = new Credenciales();
        credenciales.setUsuario(nuevaPersona.getEmail());
        credenciales.setPassword("default123");
        credenciales.setPersona(nuevaPersona);
        credencialesService.guardar(credenciales);

        return "persona";
    }
}
