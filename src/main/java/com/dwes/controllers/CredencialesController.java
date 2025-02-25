package com.dwes.controllers;

import com.dwes.serviceImpl.CredencialesServiceImpl;
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

    // Cierra sesión y redirige a login
    @GetMapping("/logout")
    public String logout() {
        return "login";
    }

    // Autenticar usuario
    @PostMapping("/autenticar")
    public String autenticar(@RequestParam String usuario, @RequestParam String password) {
        if (credencialesService.autenticar(usuario, password)) {
            return "redirect:/inicio"; // Redirige a la página de inicio después del login exitoso
        }
        return "redirect:/login?error=true"; // Redirige al login con un mensaje de error si falla la autenticación
    }
}
