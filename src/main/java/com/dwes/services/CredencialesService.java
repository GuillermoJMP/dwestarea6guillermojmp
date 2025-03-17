package com.dwes.services;

import com.dwes.models.Credenciales;
import java.util.Optional;

public interface CredencialesService {
	void guardar(Credenciales credenciales);

	Optional<Credenciales> obtenerUsuario(String usuario);

	boolean autenticar(String usuario, String password);
}
