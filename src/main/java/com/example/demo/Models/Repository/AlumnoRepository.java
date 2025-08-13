package com.example.demo.Models.Repository;

import com.example.demo.Models.Domain.Alumno;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlumnoRepository extends JpaRepository<Alumno, Long> {}
