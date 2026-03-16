package com.dwes.models;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;
import com.dwes.models.Planta;

@Entity
@Table(name = "persona")
public class Persona {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(nullable = false, unique = true)
	private String nombre;
	@Column(nullable = false, unique = true)
	private String email;
	@Column(nullable = false, unique = true)
	private String usuario;
	@Column(nullable = false)
	private String password;

	private String fechaNacimiento;
	private String nifNie;
	private String direccionEnvio;
	private String telefono;

	@ManyToMany
	@JoinTable(
	    name = "persona_favoritos",
	    joinColumns = @JoinColumn(name = "persona_id"),
	    inverseJoinColumns = @JoinColumn(name = "planta_id")
	)
	private Set<Planta> favoritos = new HashSet<>();

	public Persona() {
	}

	public Persona(String nombre, String email, String usuario, String password) {
		this.nombre = nombre;
		this.email = email;
		this.usuario = usuario;
		this.password = password;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(String fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	public String getNifNie() {
		return nifNie;
	}

	public void setNifNie(String nifNie) {
		this.nifNie = nifNie;
	}

	public String getDireccionEnvio() {
		return direccionEnvio;
	}

	public void setDireccionEnvio(String direccionEnvio) {
		this.direccionEnvio = direccionEnvio;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public Set<Planta> getFavoritos() {
		return favoritos;
	}

	public void setFavoritos(Set<Planta> favoritos) {
		this.favoritos = favoritos;
	}
}
