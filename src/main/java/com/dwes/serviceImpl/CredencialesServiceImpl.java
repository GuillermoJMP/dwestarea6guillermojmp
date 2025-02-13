package com.dwes.serviceImpl;

import com.dwes.models.Credenciales;
import com.dwes.repositories.CredencialesRepository;
import com.dwes.services.CredencialesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CredencialesServiceImpl implements CredencialesService {

	@Autowired
    private CredencialesRepository credencialesRepository;

    @Override
    public boolean autenticar(String usuario, String password) {
        Credenciales credenciales = credencialesRepository.findByUsuario(usuario);
        return credenciales != null && credenciales.getPassword().equals(password);
    }

	@Override
	public void guardar(Credenciales credenciales) {
		// TODO Auto-generated method stub
		
	}
}
