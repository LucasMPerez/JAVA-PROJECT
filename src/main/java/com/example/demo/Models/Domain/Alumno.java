package com.example.demo.Models.Domain;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter

@Entity(name = "Alumno")
public class Alumno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "persona_id", referencedColumnName = "id")
    private Persona persona;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "alumno_materia",
            joinColumns = @JoinColumn(name = "alumno_id"),
            inverseJoinColumns = @JoinColumn(name = "materia_id")
    )
    private List<Materia> materiasAprobadas = new ArrayList<>();

    public Alumno(Persona persona) {
        this.persona = persona;
    }

    public Alumno() {

    }

    public void aprobarMateria(Materia materia) {
        if (!materiasAprobadas.contains(materia)) {
            materiasAprobadas.add(materia);
        }
    }

    // Métodos para sincronización bidireccional
    public void setPersona(Persona persona) {
        if (this.persona != null && this.persona.getRolAlumno() == this) {
            this.persona.setRolAlumno(null);
        }
        this.persona = persona;
        if (persona != null && persona.getRolAlumno() != this) {
            persona.setRolAlumno(this);
        }
    }

}

