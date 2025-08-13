package com.example.demo.Models.Domain;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;
import java.time.LocalDate;

@Getter
@Setter

@Entity
@Table(name = "Persona")
public class Persona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nombre")
    private String nombre;

    //private LocalDate fechaDeNacimiento;

    @Column(name = "legajo")
    private String legajo;

    @Column(name = "email")
    private String email;

    @Column(name = "telefono")
    private String telefono;

    @Transient
    private LocalDate fechaDeNacimiento;

    @OneToOne(mappedBy = "persona", cascade = CascadeType.ALL)
    private Alumno rolAlumno;

    @OneToOne(mappedBy = "persona", cascade = CascadeType.ALL)
    private Docente rolDocente;

    public Persona(String nombre, LocalDate fechaDeNacimiento, String legajo,
                   String email, String telefono) {
        this.nombre = nombre;
        this.fechaDeNacimiento = fechaDeNacimiento;
        this.legajo = legajo;
        this.email = email;
        this.telefono = telefono;
    }

    public Persona() {

    }

    // Métodos para gestionar roles
    public Alumno convertirEnAlumno() {
        if (this.rolAlumno == null) {
            this.rolAlumno = new Alumno(this);
        }
        return this.rolAlumno;
    }

    public Docente convertirEnDocente() {
        if (this.rolDocente == null) {
            this.rolDocente = new Docente(this);
        }
        return this.rolDocente;
    }

    public boolean esAlumno() {
        return rolAlumno != null;
    }

    public boolean esDocente() {
        return rolDocente != null;
    }

    public Alumno getRolAlumno() {
        return rolAlumno;
    }

    public void setRolAlumno(Alumno rolAlumno) {
        this.rolAlumno = rolAlumno;
    }
}
