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

    @PostMapping("/guardarPersona")
    public String guardar(@RequestParam String nombre, @RequestParam String email, @RequestParam String usuario, @RequestParam String contrasena, Model model) {
        Persona persona = new Persona();
        persona.setNombre(nombre);
        persona.setEmail(email);

        Persona nuevaPersona = personaService.guardar(persona);

        Credenciales credenciales = new Credenciales();
        credenciales.setUsuario(usuario);
        credenciales.setPassword(contrasena);
        credenciales.setPersona(nuevaPersona);
        credencialesService.guardar(credenciales);

        List<Persona> personas = personaService.listarTodos();
        model.addAttribute("personas", personas);
        return "persona";
    }
}
