package com.dwes.controllers;

import com.dwes.models.Ejemplar;
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

    @GetMapping("/ejemplares")
    public String listar(Model model) {
        List<Ejemplar> ejemplares = ejemplarService.listarTodos();
        model.addAttribute("ejemplares", ejemplares);
        return "ejemplares";
    }

    @GetMapping("/nuevoEjemplar")
    public String crearFormulario(Model model) {
        model.addAttribute("ejemplar", new Ejemplar());
        model.addAttribute("plantas", plantaService.listarTodas());
        return "ejemplares";
    }

    @PostMapping("/guardarEjemplar")
    public String guardar(@ModelAttribute Ejemplar ejemplar) {
        ejemplarService.guardar(ejemplar);
        return "ejemplares";
    }

//    @GetMapping("/editar/{id}")
//    public String editar(@PathVariable Long id, Model model) {
//        model.addAttribute("ejemplar", ejemplarService.obtenerPorId(id));
//        model.addAttribute("plantas", plantaService.listarTodas());
//        return "ejemplares";
//    }
}
