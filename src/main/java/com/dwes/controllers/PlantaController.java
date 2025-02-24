package com.dwes.controllers;

import com.dwes.models.Planta;
import com.dwes.services.PlantaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.List;

@Controller
public class PlantaController {

    @Autowired
    private PlantaService plantaService;

    // Listar todas las plantas
    @GetMapping("/plantas")
    public String listar(Model model) {
        model.addAttribute("plantas", plantaService.listarTodas());
        return "plantas";
    }

    // Guardar una nueva planta
    @PostMapping("/guardarPlanta")
    public String guardar(@ModelAttribute Planta planta, Model model) {
        plantaService.guardar(planta);
        model.addAttribute("plantas", plantaService.listarTodas());
        return "plantas";
    }

    // Buscar una planta por ID y cargar la vista con sus datos
    @GetMapping("/editar/{id}")
    public String buscarPorId(@PathVariable Long id, Model model) {
        Optional<Planta> planta = plantaService.obtenerPorId(id);
        if (planta.isPresent()) {
            model.addAttribute("planta", planta.get());
        } else {
            model.addAttribute("error", "Planta no encontrada");
        }
        model.addAttribute("plantas", plantaService.listarTodas());
        return "plantas";
    }
}
