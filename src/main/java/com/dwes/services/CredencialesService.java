package com.dwes.services;

import com.dwes.models.Credenciales;

public interface CredencialesService {
    void guardar(Credenciales credenciales);
    boolean autenticar(String usuario, String password);
    Credenciales obtenerUsuario(String usuario);
}
