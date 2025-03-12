package com.dwes.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    // Métodos adicionales para Thymeleaf
    public int getNumeroMensajes() {
        return (mensajes != null) ? mensajes.size() : 0;
    }

    public String getUltimoMensaje() {
        if (mensajes != null && !mensajes.isEmpty()) {
            LocalDateTime fechaHora = mensajes.get(mensajes.size() - 1).getFechaHora();
            return fechaHora.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
        }
        return "Sin mensajes";
    }
}
