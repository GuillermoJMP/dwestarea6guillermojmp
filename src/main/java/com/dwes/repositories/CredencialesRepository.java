package com.dwes.repositories;

import com.dwes.models.Credenciales;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CredencialesRepository extends JpaRepository<Credenciales, Long> {
	Credenciales findByUsuario(String usuario);
}
