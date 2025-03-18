package com.dwes.controllers;

import com.dwes.models.Credenciales;
import com.dwes.models.Persona;
import com.dwes.services.CredencialesService;
import com.dwes.services.PersonaService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class CredencialesController {

	@Autowired
	private CredencialesService credencialesService;

	@Autowired
	private PersonaService personaService;

	@GetMapping("/login")
	public String mostrarLogin() {
		return "login";
	}

	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		SecurityContextHolder.clearContext();
		return "redirect:/inicio";
	}

	@PostMapping("/autenticar")
	public String autenticar(@RequestParam String usuario, @RequestParam String password, HttpSession session) {
		usuario = usuario.trim();
		password = password.trim();
		if (usuario.isEmpty() || password.isEmpty()) {
			return "redirect:/login?error=camposVacios";
		}
		if (!credencialesService.autenticar(usuario, password)) {
			return "redirect:/login?error=credencialesInvalidas";
		}
		Optional<Credenciales> credencialesOpt = credencialesService.obtenerUsuario(usuario);
		if (credencialesOpt.isEmpty()) {
			return "redirect:/login?error=credencialesInvalidas";
		}
		Credenciales credenciales = credencialesOpt.get();
		String rol = credenciales.getRol();

		System.out.println("Usuario autenticado: " + usuario + ", Rol: " + rol);

		UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(usuario, null,
				AuthorityUtils.createAuthorityList(rol));
		SecurityContextHolder.getContext().setAuthentication(auth);
		session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());

		session.setAttribute("usuarioLogeado", usuario);
		session.setAttribute("rol", rol);

		Persona persona = personaService.obtenerPorUsuario(usuario);
		if (persona == null) {
			return "redirect:/login?error=usuarioNoEncontrado";
		}
		session.setAttribute("usuarioId", persona.getId());

		System.out.println("Sesión creada - Usuario: " + session.getAttribute("usuarioLogeado") + ", Rol: "
				+ session.getAttribute("rol"));

		if ("CLIENTE".equals(rol)) {
			return "redirect:/zonaCliente";
		}
		return "redirect:/inicio";
	}

}
