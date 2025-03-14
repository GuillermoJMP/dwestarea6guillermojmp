package com.dwes.controllers;

import com.dwes.models.Ejemplar;
import com.dwes.models.Planta;
import com.dwes.models.Mensaje;
import com.dwes.models.Persona;
import com.dwes.services.EjemplarService;
import com.dwes.services.PlantaService;
import com.dwes.services.MensajeService;
import com.dwes.services.PersonaService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
public class EjemplarController {

    @Autowired
    private EjemplarService ejemplarService;

    @Autowired
    private PlantaService plantaService;

    @Autowired
    private MensajeService mensajeService;

    @Autowired
    private PersonaService personaService;

    // Página de gestión de ejemplares (Personal y Admin)
    @GetMapping("/ejemplaresAdmin")
    public String listar(Model model, @RequestParam(required = false) Long plantaId, HttpSession session, RedirectAttributes redirectAttributes) {
        String rol = (String) session.getAttribute("rol");

        if (rol == null || (!rol.equals("ADMIN") && !rol.equals("PERSONAL"))) {
            redirectAttributes.addFlashAttribute("errorMessage", "Acceso denegado. No tienes permisos.");
            return "redirect:/inicio";
        }

        List<Ejemplar> ejemplares = (plantaId == null) ? ejemplarService.listarTodos() : ejemplarService.filtrarPorPlanta(plantaId);
        model.addAttribute("plantas", plantaService.listarTodas());
        model.addAttribute("ejemplares", ejemplares);
        model.addAttribute("plantaSeleccionada", plantaId);

        return "ejemplaresAdmin";
    }

    // Guardar un nuevo ejemplar con generación automática del nombre y mensaje inicial
    @PostMapping("/guardarEjemplar")
    public String guardar(@RequestParam Long planta, HttpSession session, RedirectAttributes redirectAttributes) {
        String rol = (String) session.getAttribute("rol");

        if (rol == null || (!rol.equals("ADMIN") && !rol.equals("PERSONAL"))) {
            redirectAttributes.addFlashAttribute("errorMessage", "Acceso denegado. No tienes permisos.");
            return "redirect:/inicio";
        }

        Optional<Planta> optionalPlanta = plantaService.obtenerPorId(planta);

        if (optionalPlanta.isPresent()) {
            Planta selectedPlanta = optionalPlanta.get();
            Ejemplar ejemplar = new Ejemplar();
            ejemplar.setPlanta(selectedPlanta);

            // Guardar ejemplar sin nombre y obtener su ID
            ejemplar = ejemplarService.guardar(ejemplar);
            ejemplar.setNombre(selectedPlanta.getCodigo() + "_" + ejemplar.getId());

            // Guardar nuevamente con el nombre generado
            ejemplar = ejemplarService.guardar(ejemplar);

            // Obtener el usuario logueado
            String usuarioLogeado = (String) session.getAttribute("usuarioLogeado");

            // Obtener objeto Persona del usuario logueado
            Persona personaLogeada = personaService.obtenerPorId((Long) session.getAttribute("usuarioId"));

            // Registrar un mensaje inicial
            Mensaje mensajeInicial = new Mensaje();
            mensajeInicial.setEjemplar(ejemplar);
            mensajeInicial.setMensaje("Ejemplar creado por " + usuarioLogeado);
            mensajeInicial.setFechaHora(LocalDateTime.now());
            mensajeInicial.setPersona(personaLogeada);  // Corregido el set de usuario
            mensajeService.guardar(mensajeInicial);

            redirectAttributes.addFlashAttribute("successMessage", "Ejemplar registrado correctamente.");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al registrar el ejemplar.");
        }

        return "redirect:/ejemplaresAdmin";
    }

    // Ver los mensajes de un ejemplar en orden cronológico
    @GetMapping("/verMensajes/{id}")
    public String verMensajes(@PathVariable Long id, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        String rol = (String) session.getAttribute("rol");

        if (rol == null || (!rol.equals("ADMIN") && !rol.equals("PERSONAL"))) {
            redirectAttributes.addFlashAttribute("errorMessage", "Acceso denegado. No tienes permisos.");
            return "redirect:/inicio";
        }

        Optional<Ejemplar> ejemplarOptional = ejemplarService.obtenerPorId(id);

        if (ejemplarOptional.isPresent()) {
            Ejemplar ejemplar = ejemplarOptional.get();
            List<Mensaje> mensajes = mensajeService.buscarPorEjemplar(id); // Método corregido

            model.addAttribute("ejemplar", ejemplar);
            model.addAttribute("mensajes", mensajes);
            return "mensajesEjemplar";
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Ejemplar no encontrado.");
            return "redirect:/ejemplaresAdmin";
        }
    }
}
