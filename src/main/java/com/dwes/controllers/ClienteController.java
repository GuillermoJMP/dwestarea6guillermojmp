package com.dwes.controllers;

import com.dwes.models.Persona;
import com.dwes.models.Credenciales;
import com.dwes.services.PersonaService;
import com.dwes.services.CredencialesService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ClienteController {

    @Autowired
    private PersonaService personaService;

    @Autowired
    private CredencialesService credencialesService;

    @GetMapping("/registroCliente")
    public String mostrarRegistroCliente() {
        return "registroCliente";
    }

    @PostMapping("/guardarCliente")
    public String guardarCliente(@ModelAttribute Persona cliente, RedirectAttributes redirectAttributes,
                                  HttpSession session) {
        if (cliente.getNombre().trim().isEmpty() || cliente.getEmail().trim().isEmpty()
                || cliente.getUsuario().trim().isEmpty() || cliente.getPassword().trim().isEmpty()
                || cliente.getFechaNacimiento() == null || cliente.getNifNie().trim().isEmpty()
                || cliente.getDireccionEnvio().trim().isEmpty() || cliente.getTelefono().trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Todos los campos son obligatorios.");
            return "redirect:/registroCliente";
        }
        if (personaService.existeEmail(cliente.getEmail())) {
            redirectAttributes.addFlashAttribute("errorMessage", "El email ya está en uso.");
            return "redirect:/registroCliente";
        }
        if (personaService.existeUsuario(cliente.getUsuario())) {
            redirectAttributes.addFlashAttribute("errorMessage", "El nombre de usuario ya está en uso.");
            return "redirect:/registroCliente";
        }
        if (cliente.getPassword().length() < 6 || cliente.getPassword().contains(" ")) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "La contraseña debe tener al menos 6 caracteres y no contener espacios.");
            return "redirect:/registroCliente";
        }
        String originalPassword = cliente.getPassword();
        
        cliente = personaService.guardar(cliente);
        
        Credenciales cred = new Credenciales(cliente.getUsuario(), originalPassword, "CLIENTE");
        credencialesService.guardar(cred);
        
        redirectAttributes.addFlashAttribute("successMessage",
                "Cliente registrado correctamente. Por favor, inicie sesión.");
        return "redirect:/login";
    }

    @GetMapping("/zonaCliente")
    public String mostrarZonaCliente(HttpSession session, Model model) {
        String rol = (String) session.getAttribute("rol");
        if (rol == null || !rol.equals("CLIENTE")) {
            return "redirect:/login?error=noAutorizado";
        }
        model.addAttribute("mensaje", "Bienvenido a la zona cliente.");
        return "zonaCliente";
    }
}
