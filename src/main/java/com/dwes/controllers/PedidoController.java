package com.dwes.controllers;

import com.dwes.models.Ejemplar;
import com.dwes.models.Mensaje;
import com.dwes.models.Pedido;
import com.dwes.models.Persona;
import com.dwes.models.Planta;
import com.dwes.services.EjemplarService;
import com.dwes.services.MensajeService;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

	@Autowired
	private MensajeService mensajeService;

	@GetMapping("/pedidoCliente")
	public String mostrarPedidoCliente(Model model, HttpSession session, RedirectAttributes redirectAttributes) {
		List<Planta> todasPlantas = plantaService.listarTodas();
		List<Planta> plantasConStock = new ArrayList<>();
		Map<Long, Integer> stockPorPlanta = new HashMap<>();

		for (Planta p : todasPlantas) {
			int stock = ejemplarService.filtrarPorPlanta(p.getId()).stream().filter(e -> e.isDisponible()).toList()
					.size();
			if (stock > 0) {
				plantasConStock.add(p);
				stockPorPlanta.put(p.getId(), stock);
			}
		}

		List<Ejemplar> ejemplaresNoVendidos = ejemplarService.listarTodos().stream().filter(e -> e.isDisponible())
				.toList();

		model.addAttribute("plantas", plantasConStock);
		model.addAttribute("ejemplares", ejemplaresNoVendidos);
		model.addAttribute("stockPorPlanta", stockPorPlanta);

		return "pedidoCliente";
	}

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
	public String anadirAlCarrito(@RequestParam Long plantaId, @RequestParam int cantidad, HttpSession session,
			RedirectAttributes redirectAttributes) {
		List<Ejemplar> ejemplaresDisponibles = ejemplarService.filtrarPorPlanta(plantaId).stream()
				.filter(e -> e.isDisponible()).toList();

		if (ejemplaresDisponibles.size() < cantidad) {
			redirectAttributes.addFlashAttribute("errorMessage",
					"No hay suficientes ejemplares disponibles para esa planta.");
			return "redirect:/pedidoCliente";
		}

		List<Ejemplar> ejemplaresSeleccionados = ejemplaresDisponibles.subList(0, cantidad);

		List<Long> carrito = (List<Long>) session.getAttribute("carrito");
		if (carrito == null) {
			carrito = new ArrayList<>();
		}
		for (Ejemplar e : ejemplaresSeleccionados) {
			carrito.add(e.getId());
		}
		session.setAttribute("carrito", carrito);

		redirectAttributes.addFlashAttribute("successMessage",
				"Se añadieron " + cantidad + " ejemplar(es) al carrito.");
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
	        if (ejemplar != null && ejemplar.isDisponible()) {
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

	    StringBuilder sb = new StringBuilder();
	    sb.append("El usuario ")
	      .append(cliente.getNombre())
	      .append(" realizó un pedido con ")
	      .append(ejemplaresPedido.size())
	      .append(" ejemplares: ");

	    for (int i = 0; i < ejemplaresPedido.size(); i++) {
	        sb.append(ejemplaresPedido.get(i).getNombre());
	        if (i < ejemplaresPedido.size() - 1) {
	            sb.append(", ");
	        }
	    }

	    Mensaje mensajePedido = new Mensaje();
	    mensajePedido.setPersona(cliente);
	    mensajePedido.setFechaHora(LocalDateTime.now());
	    mensajePedido.setMensaje(sb.toString());

	    mensajeService.guardar(mensajePedido);

	    redirectAttributes.addFlashAttribute("successMessage", "Pedido confirmado correctamente.");
	    return "redirect:/zonaCliente";
	}


	@PostMapping("/anadirAlCarritoMultiple")
	public String anadirAlCarritoMultiple(@RequestParam List<Long> plantaIds, @RequestParam List<Integer> cantidades,
			HttpSession session, RedirectAttributes redirectAttributes) {
		if (plantaIds.size() != cantidades.size()) {
			redirectAttributes.addFlashAttribute("errorMessage", "Datos de formulario inválidos.");
			return "redirect:/pedidoCliente";
		}

		List<Long> carrito = (List<Long>) session.getAttribute("carrito");
		if (carrito == null) {
			carrito = new ArrayList<>();
		}

		for (int i = 0; i < plantaIds.size(); i++) {
			Long plantaId = plantaIds.get(i);
			int cantidad = cantidades.get(i);

			if (cantidad <= 0) {
				continue;
			}

			List<Ejemplar> ejemplaresDisponibles = ejemplarService.filtrarPorPlanta(plantaId).stream()
					.filter(Ejemplar::isDisponible).toList();

			if (ejemplaresDisponibles.size() < cantidad) {
				redirectAttributes.addFlashAttribute("errorMessage",
						"No hay suficientes ejemplares disponibles para la planta con id=" + plantaId);
				return "redirect:/pedidoCliente";
			}

			for (int j = 0; j < cantidad; j++) {
				carrito.add(ejemplaresDisponibles.get(j).getId());
			}
		}

		session.setAttribute("carrito", carrito);

		redirectAttributes.addFlashAttribute("successMessage", "Se añadieron las plantas seleccionadas al carrito.");
		return "redirect:/pedidoCliente";
	}
}
