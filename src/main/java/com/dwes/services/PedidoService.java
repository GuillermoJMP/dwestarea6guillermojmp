package com.dwes.services;

import com.dwes.models.Pedido;
import java.util.List;
import java.util.Optional;

public interface PedidoService {
	Pedido guardar(Pedido pedido);

	Optional<Pedido> obtenerPorId(Long id);

	List<Pedido> listarTodos();

	void eliminar(Long id);
}
