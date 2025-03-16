package com.dwes.controllers;

import com.dwes.models.Mensaje;
import com.dwes.models.Persona;
import com.dwes.models.Ejemplar;
import com.dwes.services.MensajeService;
import com.dwes.services.PersonaService;
import com.dwes.services.EjemplarService;
import com.dwes.services.PlantaService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
public class MensajeController {

    @Autowired
    private MensajeService mensajeService;

    @Autowired
    private PersonaService personaService;

    @Autowired
    private EjemplarService ejemplarService;
    
    @Autowired
    private PlantaService plantaService; // Para el filtro por planta

    @GetMapping("/mensajesAdmin")
    public String listar(@RequestParam(required = false) Long ejemplarId,
                         @RequestParam(required = false) Long personaId,
                         @RequestParam(required = false) String inicio,
                         @RequestParam(required = false) String fin,
                         @RequestParam(required = false) Long plantaFiltro,
                         Model model,
                         HttpSession session,
                         RedirectAttributes redirectAttributes) {

        String rol = (String) session.getAttribute("rol");
        if (rol == null || (!rol.equals("ADMIN") && !rol.equals("PERSONAL"))) {
            redirectAttributes.addFlashAttribute("errorMessage", "Acceso denegado. No tienes permisos.");
            return "redirect:/inicio";
        }
        List<Mensaje> mensajes;
        try {
            if (plantaFiltro != null) {
                mensajes = mensajeService.buscarPorPlanta(plantaFiltro);
            } else if (ejemplarId != null) {
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
            redirectAttributes.addFlashAttribute("errorMessage", "Error al filtrar mensajes.");
            return "redirect:/mensajesAdmin";
        }
        model.addAttribute("mensajes", mensajes);
        model.addAttribute("ejemplares", ejemplarService.listarTodos());
        model.addAttribute("personas", personaService.listarTodos());
        model.addAttribute("plantas", plantaService.listarTodas());
        model.addAttribute("plantaFiltro", plantaFiltro);
        return "mensajesAdmin";
    }

    @PostMapping("/guardarMensaje")
    public String guardar(@RequestParam String anotacion,
                          @RequestParam Long ejemplarId,
                          @RequestParam Long personaId,
                          HttpSession session,
                          RedirectAttributes redirectAttributes) {

        String rol = (String) session.getAttribute("rol");
        if (rol == null || (!rol.equals("ADMIN") && !rol.equals("PERSONAL"))) {
            redirectAttributes.addFlashAttribute("errorMessage", "Acceso denegado. No tienes permisos.");
            return "redirect:/inicio";
        }
        if (anotacion == null || anotacion.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "El mensaje no puede estar vacío.");
            return "redirect:/mensajesAdmin";
        }
        Persona persona = personaService.obtenerPorId(personaId);
        Optional<Ejemplar> ejemplarOptional = ejemplarService.obtenerPorId(ejemplarId);
        Ejemplar ejemplar = ejemplarOptional.orElse(null);
        if (persona == null || ejemplar == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Debe seleccionar un usuario y un ejemplar válidos.");
            return "redirect:/mensajesAdmin";
        }
        Mensaje mensaje = new Mensaje();
        mensaje.setMensaje(anotacion);
        mensaje.setFechaHora(LocalDateTime.now());
        mensaje.setPersona(persona);
        mensaje.setEjemplar(ejemplar);
        mensajeService.guardar(mensaje);
        redirectAttributes.addFlashAttribute("successMessage", "Mensaje guardado correctamente.");
        return "redirect:/mensajesAdmin";
    }
}
