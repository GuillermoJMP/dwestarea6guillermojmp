package com.dwes.controllers;

import com.dwes.models.Credenciales;
import com.dwes.services.CredencialesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Optional;

@Controller
public class CredencialesController {

	@Autowired
	private CredencialesService credencialesService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@GetMapping("/login")
	public String mostrarLogin() {
		return "login";
	}

	@GetMapping("/logout")
	public String logout() {
		SecurityContextHolder.clearContext();
		return "redirect:/inicio";
	}

	@PostMapping("/autenticar")
	public String autenticar(@RequestParam String usuario, @RequestParam String password) {
		if (usuario == null || password == null || usuario.trim().isEmpty() || password.trim().isEmpty()) {
			return "redirect:/login?error=camposVacios";
		}

		Optional<Credenciales> credencialesOpt = credencialesService.obtenerUsuario(usuario);
		if (credencialesOpt.isEmpty()) {
			return "redirect:/login?error=usuarioNoEncontrado";
		}

		Credenciales credenciales = credencialesOpt.get();

		// Verificar la contraseña encriptada
		if (!passwordEncoder.matches(password, credenciales.getPassword())) {
			return "redirect:/login?error=credencialesInvalidas";
		}

		// Asegurar que el rol tiene "ROLE_" antes de enviarlo a Spring Security
		String rol = credenciales.getRol().startsWith("ROLE_") ? credenciales.getRol()
				: "ROLE_" + credenciales.getRol();

		// Configurar autenticación en Spring Security
		UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(usuario,
				credenciales.getPassword(), Collections.singletonList(new SimpleGrantedAuthority(rol)));

		SecurityContextHolder.getContext().setAuthentication(auth);

		return "redirect:/inicio";
	}
}
