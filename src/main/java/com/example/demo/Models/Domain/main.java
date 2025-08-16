package com.example.demo.Models.Domain;

import java.time.LocalDate;
import java.util.List;
import jakarta.persistence.*;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication
@EntityScan("com.example.demo.model")
@EnableJpaRepositories("com.example.demo.Models.Repository")
public class main {
    private static EntityManagerFactory emf;

    public static void main(String[] args) {
        // Inicializar EntityManagerFactory usando la unidad de persistencia definida
        emf = Persistence.createEntityManagerFactory("simple-persistence-unit");
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();

            // Crear carreras
            Carrera ingenieriaSistemas = new Carrera("Ingeniería en Sistemas");
            Carrera licenciaturaInformatica = new Carrera("Licenciatura en Informática");

            persistir(em, ingenieriaSistemas);
            persistir(em, licenciaturaInformatica);

            // Crear materias
            Materia programacionI = new Materia("Programación I");
            Materia baseDatos = new Materia("Base de Datos");
            Materia algoritmos = new Materia("Algoritmos");
            Materia redes = new Materia("Redes");
            Materia matematica = new Materia("Matemática I");

            persistir(em, programacionI, baseDatos, algoritmos, redes, matematica);

            // Asociar materias a carreras
            programacionI.agregarCarrera(ingenieriaSistemas);
            baseDatos.agregarCarrera(ingenieriaSistemas);
            algoritmos.agregarCarrera(ingenieriaSistemas);
            redes.agregarCarrera(licenciaturaInformatica);
            matematica.agregarCarrera(licenciaturaInformatica);
            matematica.agregarCarrera(ingenieriaSistemas);  // Materia compartida

            // Actualizar relaciones en BD
            actualizar(em, programacionI, baseDatos, algoritmos, redes, matematica);
            actualizar(em, ingenieriaSistemas, licenciaturaInformatica);

            // Crear personas
            Persona persona1 = new Persona(
                    "Juan Pérez",
                    LocalDate.of(1995, 5, 15),
                    "LP12345",
                    "juan.perez@universidad.edu",
                    "555-1234"
            );

            Persona persona2 = new Persona(
                    "María García",
                    LocalDate.of(1998, 8, 22),
                    "LP54321",
                    "maria.garcia@universidad.edu",
                    "555-5678"
            );

            Persona persona3 = new Persona(
                    "Carlos López",
                    LocalDate.of(1985, 3, 10),
                    "DOC9876",
                    "carlos.lopez@universidad.edu",
                    "555-8765"
            );

            Persona persona4 = new Persona(
                    "Ana Martínez",
                    LocalDate.of(2000, 11, 30),
                    "LP11223",
                    "ana.martinez@universidad.edu",
                    "555-4321"
            );

            persistir(em, persona1, persona2, persona3, persona4);

            // Asignar roles
            Alumno alumno1 = persona1.convertirEnAlumno();
            Docente docente1 = persona1.convertirEnDocente();

            Alumno alumno2 = persona2.convertirEnAlumno();

            Docente docente2 = persona3.convertirEnDocente();

            Alumno alumno3 = persona4.convertirEnAlumno();

            // Persistir roles
            persistir(em, alumno1, docente1, alumno2, docente2, alumno3);

            // Asignar materias aprobadas a alumnos
            alumno1.aprobarMateria(programacionI);
            alumno1.aprobarMateria(matematica);

            alumno2.aprobarMateria(baseDatos);
            alumno2.aprobarMateria(algoritmos);

            alumno3.aprobarMateria(redes);

            // Actualizar alumnos
            actualizar(em, alumno1, alumno2, alumno3);

            // Asignar materias dictadas a docentes
            docente1.agregarMateriaDictada(programacionI);
            docente1.agregarMateriaDictada(algoritmos);

            docente2.agregarMateriaDictada(redes);
            docente2.agregarMateriaDictada(baseDatos);

            // Actualizar docentes y materias
            actualizar(em, docente1, docente2);
            actualizar(em, programacionI, algoritmos, redes, baseDatos);

            em.getTransaction().commit();

            System.out.println("¡Datos de prueba persistidos exitosamente!");

            // Mostrar datos persistidos
            mostrarDatosPersistidos(em);

        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
            emf.close();
        }
    }

    // Métodos auxiliares para operaciones JPA
    private static void persistir(EntityManager em, Object... entities) {
        for (Object entity : entities) {
            em.persist(entity);
        }
    }

    private static void actualizar(EntityManager em, Object... entities) {
        for (Object entity : entities) {
            em.merge(entity);
        }
    }

    // Método para mostrar los datos persistidos
    private static void mostrarDatosPersistidos(EntityManager em) {
        System.out.println("\n=== DATOS PERSISTIDOS EN LA BASE DE DATOS ===");

        // Mostrar carreras
        List<Carrera> carreras = em.createQuery("SELECT c FROM Carrera c", Carrera.class).getResultList();
        System.out.println("\nCarreras (" + carreras.size() + "):");
        for (Carrera c : carreras) {
            System.out.println(" - " + c.getNombre() + " (ID: " + c.getId() + ")");
            System.out.println("   Materias: " + c.getMaterias().size());
        }

        // Mostrar materias
        List<Materia> materias = em.createQuery("SELECT m FROM Materia m", Materia.class).getResultList();
        System.out.println("\nMaterias (" + materias.size() + "):");
        for (Materia m : materias) {
            System.out.print(" - " + m.getNombre() + " (ID: " + m.getId() + ")");
            if (m.getDocente() != null) {
                System.out.print(" | Docente: " + m.getDocente().getPersona().getNombre());
            }
            System.out.println(" | Carreras: " + m.getCarreras().size());
        }

        // Mostrar personas
        List<Persona> personas = em.createQuery("SELECT p FROM Persona p", Persona.class).getResultList();
        System.out.println("\nPersonas (" + personas.size() + "):");
        for (Persona p : personas) {
            System.out.println("\n- " + p.getNombre() + " (Legajo: " + p.getLegajo() + ")");
            System.out.println("  Email: " + p.getEmail() + " | Tel: " + p.getTelefono());
            System.out.println("  Fecha Nac: " + p.getFechaDeNacimiento());

            if (p.esAlumno()) {
                Alumno alumno = p.getRolAlumno();
                System.out.println("  [ALUMNO] Materias aprobadas: " + alumno.getMateriasAprobadas().size());
            }

            if (p.esDocente()) {
                Docente docente = p.getRolDocente();
                System.out.println("  [DOCENTE] Materias dictadas: " + docente.getMateriasDictadas().size());
            }
        }
    }
}
