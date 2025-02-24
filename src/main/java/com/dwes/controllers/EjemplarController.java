package com.dwes.controllers;

import com.dwes.models.Ejemplar;
import com.dwes.models.Planta;
import com.dwes.services.EjemplarService;
import com.dwes.services.PlantaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class EjemplarController {

    @Autowired
    private EjemplarService ejemplarService;

    @Autowired
    private PlantaService plantaService;

    // Muestra la lista de ejemplares y plantas
    @GetMapping("/ejemplares")
    public String listar(Model model) {
        List<Ejemplar> ejemplares = ejemplarService.listarTodos();
        model.addAttribute("plantas", plantaService.listarTodas());
        model.addAttribute("ejemplares", ejemplares);
        return "ejemplares";
    }

    // Guarda un nuevo ejemplar y lo asocia a una planta
    @PostMapping("/guardarEjemplar")
    public String guardar(@RequestParam Long planta, Model model) {
        Planta p = new Planta();
        p.setId(planta);
        Ejemplar ejemplar = new Ejemplar();
        ejemplar.setPlanta(p);
        ejemplarService.guardar(ejemplar);

        model.addAttribute("plantas", plantaService.listarTodas());
        model.addAttribute("ejemplares", ejemplarService.listarTodos());

        return "ejemplares";
    }
}
