package com.dwes.controllers;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class ManejadorExcepciones {

	@ExceptionHandler(Exception.class)
	public String manejarExcepcion(Exception ex, RedirectAttributes redirectAttributes) {
		redirectAttributes.addFlashAttribute("errorMessage", "Se ha producido un error: " + ex.getMessage());
		return "redirect:/inicio";
	}
}
