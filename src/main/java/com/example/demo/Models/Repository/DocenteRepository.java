package com.example.demo.Models.Repository;

import com.example.demo.Models.Domain.Docente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocenteRepository extends JpaRepository<Docente, Long> {}
