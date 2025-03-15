package com.dwes.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Ejemplar {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String nombre;

	@ManyToOne
	@JoinColumn(name = "idPlanta")
	private Planta planta;

	@OneToMany(mappedBy = "ejemplar", cascade = CascadeType.ALL)
	private List<Mensaje> mensajes;

	@Transient
	private int numeroMensajes;

	@Transient
	private LocalDateTime ultimoMensaje;

	// Getters y Setters
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

	public Planta getPlanta() {
		return planta;
	}

	public void setPlanta(Planta planta) {
		this.planta = planta;
	}

	public List<Mensaje> getMensajes() {
		return mensajes;
	}

	public void setMensajes(List<Mensaje> mensajes) {
		this.mensajes = mensajes;
	}

	public int getNumeroMensajes() {
		return numeroMensajes;
	}

	public void setNumeroMensajes(int numeroMensajes) {
		this.numeroMensajes = numeroMensajes;
	}

	public LocalDateTime getUltimoMensaje() {
		return ultimoMensaje;
	}

	public void setUltimoMensaje(LocalDateTime ultimoMensaje) {
		this.ultimoMensaje = ultimoMensaje;
	}
}
