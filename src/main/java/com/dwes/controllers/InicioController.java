package com.dwes.controllers;

import com.dwes.models.Planta;
import com.dwes.services.PlantaService;
import jakarta.servlet.http.HttpSession;
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
    public String mostrarInicio(Model model, HttpSession session) {
        List<Planta> plantas = plantaService.listarTodas();
        List<Ejemplar> ejemplares = ejemplarService.listarTodos();
        
        model.addAttribute("plantas", plantas);
        model.addAttribute("ejemplares", ejemplares); // Añadir ejemplares al modelo

        String usuarioLogeado = (String) session.getAttribute("usuarioLogeado");

        if (usuarioLogeado != null) {
            model.addAttribute("nombreUsuario", usuarioLogeado);
            model.addAttribute("mensajeBienvenida", "Bienvenido, " + usuarioLogeado);
        } else {
            model.addAttribute("nombreUsuario", "Visitante");
            model.addAttribute("mensajeBienvenida", "Bienvenido a Viveros Acme. Inicia sesión para más opciones.");
        }

        return "inicio";
    }

}
