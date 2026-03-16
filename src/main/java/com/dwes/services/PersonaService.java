package com.dwes.services;

import com.dwes.models.Persona;
import java.util.List;

public interface PersonaService {
	List<Persona> listarTodos();

	Persona guardar(Persona persona);

	Persona actualizar(Persona persona);

	void eliminar(Long id);

	Persona obtenerPorId(Long id);

	boolean existeEmail(String email);

	boolean existeUsuario(String usuario);

	Persona obtenerPorUsuario(String usuario);
}
