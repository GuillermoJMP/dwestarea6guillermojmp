package com.dwes.controllers;

import com.dwes.models.Planta;
import com.dwes.services.PlantaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class InicioController {

    @Autowired
    private PlantaService plantaService;

    // Página de inicio
    @GetMapping("/inicio")
    public String mostrarInicio(Model model) {
        List<Planta> plantas = plantaService.listarTodas();
        model.addAttribute("plantas", plantas);

        // Simulación de usuario (cambiar esto con autenticación real)
        model.addAttribute("nombreUsuario", "Administrador");

        return "inicio";
    }
}
