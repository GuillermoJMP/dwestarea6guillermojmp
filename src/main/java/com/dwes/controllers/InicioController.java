package com.dwes.controllers;

import com.dwes.models.Planta;
import com.dwes.services.PlantaService;
import com.dwes.models.Persona;
import com.dwes.services.PersonaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.util.List;

@Controller
public class InicioController {

    @Autowired
    private PlantaService plantaService;

    @Autowired
    private PersonaService personaService;

    @GetMapping("/")
    public String mostrarInicio(Model model, @SessionAttribute(name = "usuario", required = false) Persona usuario) {
        List<Planta> plantas = plantaService.listarTodas();
        model.addAttribute("plantas", plantas);

        // Enviar solo el nombre en vez de un objeto Persona
        if (usuario != null) {
            model.addAttribute("nombreUsuario", usuario.getNombre());
        } else {
            model.addAttribute("nombreUsuario", "Administrador");
        }

        return "inicio";
    }
}
