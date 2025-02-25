package com.dwes.controllers;

import com.dwes.models.Planta;
import com.dwes.services.PlantaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class PlantaController {

	@Autowired
    private PlantaService plantaService;

    @GetMapping("/plantas")
    public String listar(Model model) {
        model.addAttribute("plantas", plantaService.listarTodas());
        return "plantas";
    }

    @PostMapping("/plantas")
    public String guardar(@ModelAttribute Planta planta, Model model) {
        plantaService.guardar(planta);
        model.addAttribute("plantas",plantaService.listarTodas());
        return "plantas";
    }

    @GetMapping("/plantas{id}")
    public String buscarPorId(@PathVariable Long id, Model model) {
        model.addAttribute("planta", plantaService.obtenerPorId(id));
        model.addAttribute("plantas",plantaService.listarTodas());
        return "plantas";
    }
}
