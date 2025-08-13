package com.example.demo.Service;

import com.example.demo.Models.Domain.*;
import com.example.demo.Models.Repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Configuration
public class DataLoader {

    @Bean
    @Transactional
    public CommandLineRunner initDatabase(
            PersonaRepository personaRepo,
            AlumnoRepository alumnoRepo,
            DocenteRepository docenteRepo,
            MateriaRepository materiaRepo,
            CarreraRepository carreraRepo) {

        return args -> {
            // Crear personas
            Persona persona1 = new Persona();
            persona1.setNombre("María García");
            persona1.setFechaDeNacimiento(LocalDate.of(1990, 5, 15));
            persona1.setLegajo("MG123");
            persona1.setEmail("maria@uni.edu");
            persona1.setTelefono("555-1234");
            personaRepo.save(persona1);

            Persona persona2 = new Persona();
            persona2.setNombre("Carlos López");
            persona2.setFechaDeNacimiento(LocalDate.of(1985, 8, 22));
            persona2.setLegajo("CL456");
            persona2.setEmail("carlos@uni.edu");
            persona2.setTelefono("555-5678");
            personaRepo.save(persona2);

            // Convertir en roles
            Alumno alumnoMaria = persona1.convertirEnAlumno();
            alumnoRepo.save(alumnoMaria);

            Docente docenteCarlos = persona2.convertirEnDocente();
            docenteRepo.save(docenteCarlos);

            // Crear materias
            Materia matematica = new Materia();
            matematica.setNombre("Matemáticas Avanzadas");
            materiaRepo.save(matematica);

            Materia programacion = new Materia();
            programacion.setNombre("Programación Orientada a Objetos");
            materiaRepo.save(programacion);

            // Asignar docente a materias
            docenteCarlos.agregarMateriaDictada(matematica);
            docenteCarlos.agregarMateriaDictada(programacion);
            docenteRepo.save(docenteCarlos);

            // Aprobar materias
            alumnoMaria.aprobarMateria(matematica);
            alumnoRepo.save(alumnoMaria);

            // Crear carreras
            Carrera sistemas = new Carrera();
            sistemas.setNombre("Ingeniería en Sistemas");
            carreraRepo.save(sistemas);

            Carrera informatica = new Carrera();
            informatica.setNombre("Licenciatura en Informática");
            carreraRepo.save(informatica);

            // Asignar materias a carreras
            matematica.agregarCarrera(sistemas);
            programacion.agregarCarrera(sistemas);
            programacion.agregarCarrera(informatica);
            materiaRepo.save(matematica);
            materiaRepo.save(programacion);

            System.out.println("¡Datos de prueba cargados exitosamente!");
        };
    }
}