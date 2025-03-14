package com.dwes.controllers;

import com.dwes.models.Credenciales;
import com.dwes.serviceImpl.CredencialesServiceImpl;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class CredencialesController {

    @Autowired
    private CredencialesServiceImpl credencialesService;

    // Muestra la página de login
    @GetMapping("/login")
    public String mostrarLogin() {
        return "login";
    }

    // Cierra sesión y redirige al inicio
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/inicio";
    }

    // Autentica al usuario
    @PostMapping("/autenticar")
    public String autenticar(@RequestParam String usuario, @RequestParam String password, HttpSession session) {
        if (usuario == null || password == null || usuario.trim().isEmpty() || password.trim().isEmpty()) {
            return "redirect:/login?error=camposVacios";
        }

        Credenciales credenciales = credencialesService.obtenerUsuario(usuario);

        if (credenciales == null || !credenciales.getPassword().equals(password)) {
            return "redirect:/login?error=credencialesInvalidas";
        }

        session.setAttribute("usuarioLogeado", usuario);
        session.setAttribute("rol", credenciales.getRol());

        return "redirect:/inicio";
    }
}
