package com.dwes.controllers;

import com.dwes.models.Mensaje;
import com.dwes.services.MensajeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
public class MensajeController {

    @Autowired
    private MensajeService mensajeService;

    @GetMapping("/mensajes")
    public String listar(Model model) {
        model.addAttribute("mensajes", mensajeService.listarTodos());
        return "mensaje";
    }

    @PostMapping("/guardarMensaje")
    public String guardar(@RequestParam String anotacion, Model model) {
        Mensaje mensaje = new Mensaje();
        mensaje.setMensaje(anotacion);
        mensaje.setFechaHora(LocalDateTime.now());
        mensajeService.guardar(mensaje);
        model.addAttribute("mensajes", mensajeService.listarTodos());
        return "mensaje";
    }

    @GetMapping("/mensajes/persona/{id}")
    public String listarPorPersona(@PathVariable Long id, Model model) {
        model.addAttribute("mensajes", mensajeService.buscarPorPersona(id));
        return "mensaje";
    }

    @GetMapping("/mensajes/rango")
    public String listarPorRango(@RequestParam("inicio") String inicio, @RequestParam("fin") String fin, Model model) {
        LocalDateTime fechaInicio = LocalDateTime.parse(inicio);
        LocalDateTime fechaFin = LocalDateTime.parse(fin);
        model.addAttribute("mensajes", mensajeService.buscarPorRangoFechas(fechaInicio, fechaFin));
        return "mensaje";
    }
}
