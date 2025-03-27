package com.dwes.controllers;

import com.dwes.models.Credenciales;
import com.dwes.models.Persona;
import com.dwes.services.CredencialesService;
import com.dwes.services.PersonaService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@Controller
public class CredencialesController {

    @Autowired
    private CredencialesService credencialesService;

    @Autowired
    private PersonaService personaService;

    @GetMapping("/login")
    public String mostrarLogin(Model model) {
        if (!model.containsAttribute("credenciales")) {
            model.addAttribute("credenciales", new Credenciales());
        }
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        SecurityContextHolder.clearContext();
        return "redirect:/inicio";
    }

    @PostMapping("/autenticar")
    public String autenticar(@Valid @ModelAttribute("credenciales") Credenciales credenciales,
                             BindingResult bindingResult,
                             Model model,
                             HttpSession session) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("errorMessage", "Verifica los campos.");
            return "login";
        }
        String usuario = credenciales.getUsuario().trim();
        String password = credenciales.getPassword().trim();
        if (!credencialesService.autenticar(usuario, password)) {
            model.addAttribute("errorMessage", "Usuario o contraseña incorrectos.");
            return "login";
        }
        Optional<Credenciales> credencialesOpt = credencialesService.obtenerUsuario(usuario);
        if (credencialesOpt.isEmpty()) {
            model.addAttribute("errorMessage", "Credenciales inválidas.");
            return "login";
        }
        Credenciales cred = credencialesOpt.get();
        String rol = cred.getRol();
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(usuario, null,
                AuthorityUtils.createAuthorityList(rol));
        SecurityContextHolder.getContext().setAuthentication(auth);
        session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());
        session.setAttribute("usuarioLogeado", usuario);
        session.setAttribute("rol", rol);
        Persona persona = personaService.obtenerPorUsuario(usuario);
        if (persona == null) {
            model.addAttribute("errorMessage", "Usuario no encontrado.");
            return "login";
        }
        session.setAttribute("usuarioId", persona.getId());
        if ("CLIENTE".equals(rol)) {
            return "redirect:/zonaCliente";
        }
        return "redirect:/inicio";
    }
}
