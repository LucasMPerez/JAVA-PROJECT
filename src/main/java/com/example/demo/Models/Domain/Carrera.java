package com.example.demo.Models.Domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter

@Entity(name = "Carrera")
public class Carrera {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nombre")
    private String nombre;

    @ManyToMany(mappedBy = "carreras", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Materia> materias = new ArrayList<>();

    public Carrera(String nombre) {
        this.nombre = nombre;
    }

    public Carrera() {}

    public void agregarMateria(Materia materia) {
        if (!materias.contains(materia)) {
            materias.add(materia);
            if (!materia.getCarreras().contains(this)) {
                materia.getCarreras().add(this);
            }
        }
    }
}
