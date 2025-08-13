package com.example.demo.Models.Repository;

import com.example.demo.Models.Domain.Carrera;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarreraRepository extends JpaRepository<Carrera, Long> {
    Carrera findByNombre(String nombre);
}