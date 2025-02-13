package com.dwes.controllers;

import com.dwes.models.Mensaje;
import com.dwes.services.MensajeService;
import com.dwes.services.PersonaService;
import com.dwes.services.EjemplarService;

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
    @Autowired
    private PersonaService personaService;
    @Autowired
    private EjemplarService ejemplarService;

    @GetMapping("/mensaje")
    public String listar(Model model) {
        List<Mensaje> mensajes = mensajeService.listarTodos();
        model.addAttribute("mensajes", mensajes);
        return "mensaje";
    }

    @GetMapping("/nuevoMensaje")
    public String crearFormulario(Model model) {
        model.addAttribute("mensaje", new Mensaje());
        model.addAttribute("personas", personaService.listarTodos());
        model.addAttribute("ejemplares", ejemplarService.listarTodos());
        return "mensaje";
    }

    @PostMapping("/guardarMensaje")
    public String guardar(@ModelAttribute Mensaje mensaje) {
        mensaje.setFechaHora(LocalDateTime.now());
        mensajeService.guardar(mensaje);
        return "mensaje";
    }

    @GetMapping("/persona/{id}")
    public String listarPorPersona(@PathVariable Long id, Model model) {
        List<Mensaje> mensajes = mensajeService.buscarPorPersona(id);
        model.addAttribute("mensajes", mensajes);
        return "mensaje";
    }

    @GetMapping("/ejemplar/{id}")
    public String listarPorEjemplar(@PathVariable Long id, Model model) {
        List<Mensaje> mensajes = mensajeService.buscarPorEjemplar(id);
        model.addAttribute("mensajes", mensajes);
        return "mensajes";
    }

    @GetMapping("/rango")
    public String listarPorRango(@RequestParam("inicio") String inicio, @RequestParam("fin") String fin, Model model) {
        LocalDateTime fechaInicio = LocalDateTime.parse(inicio);
        LocalDateTime fechaFin = LocalDateTime.parse(fin);
        List<Mensaje> mensajes = mensajeService.buscarPorRangoFechas(fechaInicio, fechaFin);
        model.addAttribute("mensajes", mensajes);
        return "mensajes";
    }
}
