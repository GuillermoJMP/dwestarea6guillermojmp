package com.dwes.controllers;

import com.dwes.serviceImpl.CredencialesServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class CredencialesController {

	@Autowired
	private CredencialesServiceImpl credencialesService;

	@GetMapping("/login")
	public String mostrarLogin() {
		return "login";
	}

	@GetMapping("/logout")
	public String logout() {
		return "inicio";
	}

	@PostMapping("/autenticar")
	public String autenticar(@RequestParam String usuario, @RequestParam String password) {
		if (credencialesService.autenticar(usuario, password)) {
			return "inicio";
		}
		return "inicio";
	}
}
