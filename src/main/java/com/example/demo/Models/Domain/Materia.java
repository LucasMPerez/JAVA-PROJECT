package com.example.demo.Models.Domain;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter

@Entity
@Table(name = "Materia")
public class Materia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nombre")
    private String nombre;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "docente_Id", referencedColumnName = "id")
    private Docente docente;

    @ManyToMany(mappedBy = "materiasAprobadas", fetch = FetchType.LAZY) // Mapeo bidireccional
    private List<Alumno> alumnos = new ArrayList<>();

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "materia_carrera",
            joinColumns = @JoinColumn(name = "materia_id"),
            inverseJoinColumns = @JoinColumn(name = "carrera_id")
    )
    private List<Carrera> carreras = new ArrayList<>();

    public Materia(String nombre) {
        this.nombre = nombre;
    }

    public Materia() {}

    // Métodos para sincronización bidireccional
    public void setDocente(Docente docente) {
        if (this.docente != null) {
            this.docente.getMateriasDictadas().remove(this);
        }
        this.docente = docente;
        if (docente != null && !docente.getMateriasDictadas().contains(this)) {
            docente.getMateriasDictadas().add(this);
        }
    }

    // Relación con carreras
    public void agregarCarrera(Carrera carrera) {
        if (!carreras.contains(carrera)) {
            carreras.add(carrera);
            carrera.agregarMateria(this);
        }
    }

}
