package com.dwes.controllers;

import com.dwes.models.Ejemplar;
import com.dwes.models.Pedido;
import com.dwes.models.Persona;
import com.dwes.models.Planta;
import com.dwes.services.EjemplarService;
import com.dwes.services.PedidoService;
import com.dwes.services.PersonaService;
import com.dwes.services.PlantaService;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
public class PedidoController {

	@Autowired
	private PedidoService pedidoService;

	@Autowired
	private EjemplarService ejemplarService;

	@Autowired
	private PersonaService personaService;
	
	@Autowired
	private PlantaService plantaService;

	@GetMapping("/carrito")
	public String mostrarCarrito(HttpSession session, Model model) {
		List<Long> carrito = (List<Long>) session.getAttribute("carrito");
		List<Ejemplar> ejemplares = new ArrayList<>();
		if (carrito != null) {
			for (Long id : carrito) {
				ejemplares.add(ejemplarService.obtenerPorId(id).orElse(null));
			}
		}
		model.addAttribute("ejemplaresCarrito", ejemplares);
		return "carrito";
	}

	@PostMapping("/anadirAlCarrito")
	public String anadirAlCarrito(@RequestParam Long ejemplarId, HttpSession session,
			RedirectAttributes redirectAttributes) {
		List<Long> carrito = (List<Long>) session.getAttribute("carrito");
		if (carrito == null) {
			carrito = new ArrayList<>();
		}
		carrito.add(ejemplarId);
		session.setAttribute("carrito", carrito);
		redirectAttributes.addFlashAttribute("successMessage", "Ejemplar añadido al carrito.");
		return "redirect:/carrito";
	}

	@PostMapping("/confirmarPedido")
	public String confirmarPedido(HttpSession session, RedirectAttributes redirectAttributes) {
		List<Long> carrito = (List<Long>) session.getAttribute("carrito");
		if (carrito == null || carrito.isEmpty()) {
			redirectAttributes.addFlashAttribute("errorMessage", "El carrito está vacío.");
			return "redirect:/carrito";
		}
		List<Ejemplar> ejemplaresPedido = new ArrayList<>();
		for (Long id : carrito) {
			Ejemplar ejemplar = ejemplarService.obtenerPorId(id).orElse(null);
			if (ejemplar != null && !ejemplar.isVendido()) {
				ejemplaresPedido.add(ejemplar);
			}
		}
		if (ejemplaresPedido.isEmpty()) {
			redirectAttributes.addFlashAttribute("errorMessage", "No hay ejemplares disponibles en el carrito.");
			return "redirect:/carrito";
		}
		Persona cliente = personaService.obtenerPorId((Long) session.getAttribute("usuarioId"));
		Pedido pedido = new Pedido();
		pedido.setFechaCreacion(LocalDateTime.now());
		pedido.setEstado("CONFIRMADO");
		pedido.setCliente(cliente);
		pedido.setEjemplares(ejemplaresPedido);
		pedidoService.guardar(pedido);
		for (Ejemplar ejemplar : ejemplaresPedido) {
			ejemplar.setVendido(true);
			ejemplarService.guardar(ejemplar);
		}
		session.removeAttribute("carrito");
		redirectAttributes.addFlashAttribute("successMessage", "Pedido confirmado correctamente.");
		return "redirect:/zonaCliente";
	}
	


}
