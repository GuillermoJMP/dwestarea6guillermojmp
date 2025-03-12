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

    @GetMapping("/ejemplaresAdmin")
    public String listar(Model model, @RequestParam(required = false) Long plantaId) {
        List<Ejemplar> ejemplares = (plantaId == null) ? ejemplarService.listarTodos() : ejemplarService.filtrarPorPlanta(plantaId);
        model.addAttribute("plantas", plantaService.listarTodas());
        model.addAttribute("ejemplares", ejemplares);
        return "ejemplaresAdmin";
    }

    @PostMapping("/guardarEjemplar")
    public String guardar(@RequestParam Long planta, Model model) {
        Optional<Planta> optionalPlanta = plantaService.obtenerPorId(planta);

        if (optionalPlanta.isPresent()) {
            Planta selectedPlanta = optionalPlanta.get();
            Ejemplar ejemplar = new Ejemplar();
            ejemplar.setPlanta(selectedPlanta);

            // Generar el nombre en formato CODIGO_PLANTA + ID
            ejemplar = ejemplarService.guardar(ejemplar);
            ejemplar.setNombre(selectedPlanta.getCodigo() + "_" + ejemplar.getId());
            ejemplarService.guardar(ejemplar);
        }

        return "redirect:/ejemplaresAdmin";
    }
}
