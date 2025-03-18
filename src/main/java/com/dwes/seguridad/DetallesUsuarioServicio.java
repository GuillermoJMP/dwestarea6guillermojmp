package com.dwes.seguridad;

import com.dwes.models.Credenciales;
import com.dwes.repositories.CredencialesRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.Collections;

@Service
public class DetallesUsuarioServicio implements UserDetailsService {

	private final CredencialesRepository credencialesRepository;

	public DetallesUsuarioServicio(CredencialesRepository credencialesRepository) {
		this.credencialesRepository = credencialesRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Credenciales credenciales = credencialesRepository.findByUsuario(username);
		if (credenciales == null) {
			throw new UsernameNotFoundException("Usuario no encontrado");
		}

		// 🔥 Importante: Usamos el rol tal cual SIN agregar "ROLE_"
		return new User(
				credenciales.getUsuario(), 
				credenciales.getPassword(), 
				Collections.singletonList(
					new org.springframework.security.core.authority.SimpleGrantedAuthority(credenciales.getRol())
				)
		);
	}
}
