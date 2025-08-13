package com.example.demo.Models.Repository;

import com.example.demo.Models.Domain.Persona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonaRepository extends JpaRepository<Persona, Long> {
    Persona findByLegajo(String legajo);
}
