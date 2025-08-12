package com.example.demo.Models.Domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter

@Entity(name = "Docente")
public class Docente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "persona_id", referencedColumnName = "id")
    private Persona persona;

    @OneToMany(mappedBy = "docente", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Materia> materiasDictadas = new ArrayList<>();

    public Docente(Persona persona) {
        if (this.persona != null && this.persona.getRolDocente() == this) {
            this.persona.setRolDocente(null);
        }
        this.persona = persona;
        if (persona != null && persona.getRolDocente() != this) {
            persona.setRolDocente(this);
        }

    }

    public Docente() {

    }

    public void agregarMateriaDictada(Materia materia) {
        materiasDictadas.add(materia);
        materia.setDocente(this);
    }

}

