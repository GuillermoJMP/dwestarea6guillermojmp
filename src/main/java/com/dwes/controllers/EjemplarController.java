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
import java.util.Optional;

@Controller
public class EjemplarController {

    @Autowired
    private EjemplarService ejemplarService;

    @Autowired
    private PlantaService plantaService;

    // Listar ejemplares y plantas disponibles
    @GetMapping("/ejemplares")
    public String listar(Model model) {
        List<Ejemplar> ejemplares = ejemplarService.listarTodos();
        model.addAttribute("plantas", plantaService.listarTodas());
        model.addAttribute("ejemplares", ejemplares);
        return "ejemplares";
    }

    // Guardar nuevo ejemplar
    @PostMapping("/guardarEjemplar")
    public String guardar(@RequestParam String nombre, @RequestParam Long planta, Model model) {
        Optional<Planta> optionalPlanta = plantaService.obtenerPorId(planta);
        
        if (optionalPlanta.isPresent()) {
            Ejemplar ejemplar = new Ejemplar();
            ejemplar.setNombre(nombre);
            ejemplar.setPlanta(optionalPlanta.get());
            ejemplarService.guardar(ejemplar);
        }

        model.addAttribute("plantas", plantaService.listarTodas());
        model.addAttribute("ejemplares", ejemplarService.listarTodos());
        return "ejemplares";
    }
}
