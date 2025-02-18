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
@RequestMapping("/ejemplares")
public class EjemplarController {

    @Autowired
    private EjemplarService ejemplarService;

    @Autowired
    private PlantaService plantaService;

    // Listar ejemplares y mostrar las plantas disponibles para asignar
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("ejemplares", ejemplarService.listarTodos());
        model.addAttribute("plantas", plantaService.listarTodas());
        return "ejemplares";
    }

    // Guardar nuevo ejemplar asignado a una planta
    @PostMapping("/guardar")
    public String guardar(@RequestParam Long planta, Model model) {
        Optional<Planta> optionalPlanta = plantaService.obtenerPorId(planta);

        if (optionalPlanta.isPresent()) {
            Ejemplar ejemplar = new Ejemplar();
            ejemplar.setPlanta(optionalPlanta.get());
            ejemplarService.guardar(ejemplar);
        }

        return "redirect:/ejemplares";
    }
}
