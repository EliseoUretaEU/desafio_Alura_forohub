package com.alura.desafio_forohub.repository;

import com.alura.desafio_forohub.model.Mensaje;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IMensajeRepository extends JpaRepository<Mensaje, Long> {

    void deleteById(Long id);
}
