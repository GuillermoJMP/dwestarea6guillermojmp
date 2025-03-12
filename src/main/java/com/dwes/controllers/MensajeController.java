package com.dwes.controllers;

import com.dwes.models.Mensaje;
import com.dwes.models.Persona;
import com.dwes.models.Ejemplar;
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

    // Página de Mensajes con filtro integrado
    @GetMapping("/mensajesAdmin")
    public String listar(@RequestParam(required = false) Long ejemplarId,
                         @RequestParam(required = false) Long personaId,
                         @RequestParam(required = false) String inicio,
                         @RequestParam(required = false) String fin,
                         Model model) {

        List<Mensaje> mensajes;

        try {
            if (ejemplarId != null) {
                mensajes = mensajeService.buscarPorEjemplar(ejemplarId);
            } else if (personaId != null) {
                mensajes = mensajeService.buscarPorPersona(personaId);
            } else if (inicio != null && fin != null && !inicio.isEmpty() && !fin.isEmpty()) {
                LocalDateTime fechaInicio = LocalDate.parse(inicio).atStartOfDay();
                LocalDateTime fechaFin = LocalDate.parse(fin).atTime(23, 59, 59);
                mensajes = mensajeService.buscarPorRangoFechas(fechaInicio, fechaFin);
            } else {
                mensajes = mensajeService.listarTodos();
            }
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error al filtrar mensajes.");
            mensajes = mensajeService.listarTodos();
        }

        model.addAttribute("mensajes", mensajes);
        model.addAttribute("ejemplares", ejemplarService.listarTodos());
        model.addAttribute("personas", personaService.listarTodos());
        return "mensajesAdmin"; // Corregido para coincidir con la vista
    }

    // Guardar mensaje desde la misma página
    @PostMapping("/guardarMensaje")
    public String guardar(@RequestParam String anotacion,
                          @RequestParam Long ejemplarId,
                          @RequestParam Long personaId,
                          Model model) {

        if (anotacion == null || anotacion.trim().isEmpty()) {
            model.addAttribute("errorMessage", "El mensaje no puede estar vacío.");
            return "redirect:/mensajesAdmin";
        }

        Persona persona = personaService.obtenerPorId(personaId);
        Ejemplar ejemplar = ejemplarService.obtenerPorId(ejemplarId);

        if (persona == null || ejemplar == null) {
            model.addAttribute("errorMessage", "Debe seleccionar un usuario y un ejemplar válidos.");
            return "redirect:/mensajesAdmin";
        }

        Mensaje mensaje = new Mensaje();
        mensaje.setMensaje(anotacion);
        mensaje.setFechaHora(LocalDateTime.now());
        mensaje.setPersona(persona);
        mensaje.setEjemplar(ejemplar);
        mensajeService.guardar(mensaje);

        return "redirect:/mensajesAdmin";
    }
}
