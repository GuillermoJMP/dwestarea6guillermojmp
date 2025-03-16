package com.dwes.serviceImpl;

import com.dwes.models.Pedido;
import com.dwes.repositories.PedidoRepository;
import com.dwes.services.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class PedidoServiceImpl implements PedidoService {

	@Autowired
	private PedidoRepository pedidoRepository;

	@Override
	public Pedido guardar(Pedido pedido) {
		return pedidoRepository.save(pedido);
	}

	@Override
	public Optional<Pedido> obtenerPorId(Long id) {
		return pedidoRepository.findById(id);
	}

	@Override
	public List<Pedido> listarTodos() {
		return pedidoRepository.findAll();
	}

	@Override
	public void eliminar(Long id) {
		pedidoRepository.deleteById(id);
	}
}
