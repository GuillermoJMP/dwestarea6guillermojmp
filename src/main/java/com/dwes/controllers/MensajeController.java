package com.dwes.controllers;

import com.dwes.models.Mensaje;
import com.dwes.services.MensajeService;
import com.dwes.services.PersonaService;
import com.dwes.services.EjemplarService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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

    // Listar todos los mensajes
    @GetMapping("/mensaje")
    public String listar(Model model) {
        List<Mensaje> mensajes = mensajeService.listarTodos();
        model.addAttribute("mensajes", mensajes);
        return "mensaje";
    }

    // Guardar un nuevo mensaje
    @PostMapping("/crearMensaje")
    public String guardar(@RequestParam String anotacion, Model model) {
        Mensaje mensaje = new Mensaje();
        mensaje.setMensaje(anotacion);
        mensaje.setFechaHora(LocalDateTime.now());
        mensajeService.guardar(mensaje);

        model.addAttribute("mensajes", mensajeService.listarTodos());
        return "mensaje";
    }

    // Buscar mensajes por persona
    @GetMapping("/persona/{id}")
    public String listarPorPersona(@PathVariable Long id, Model model) {
        List<Mensaje> mensajes = mensajeService.buscarPorPersona(id);
        model.addAttribute("mensajes", mensajes);
        return "mensaje";
    }

    // Buscar mensajes por rango de fechas
    @GetMapping("/rango")
    public String listarPorRango(@RequestParam(value = "inicio", required = false) String inicio,
                                 @RequestParam(value = "fin", required = false) String fin,
                                 Model model) {
        LocalDateTime fechaInicio = null;
        LocalDateTime fechaFin = null;

        try {
            if (inicio != null && !inicio.isEmpty()) {
                fechaInicio = LocalDate.parse(inicio).atStartOfDay();
            }
            if (fin != null && !fin.isEmpty()) {
                fechaFin = LocalDate.parse(fin).atTime(23, 59, 59);
            }
        } catch (Exception e) {
            model.addAttribute("error", "Formato de fecha incorrecto");
            return "mensaje";
        }

        List<Mensaje> mensajes = mensajeService.buscarPorRangoFechas(fechaInicio, fechaFin);
        model.addAttribute("mensajes", mensajes);
        return "mensaje";
    }
}
