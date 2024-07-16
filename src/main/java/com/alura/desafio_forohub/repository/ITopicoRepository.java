package com.alura.desafio_forohub.repository;

import com.alura.desafio_forohub.model.Curso;
import com.alura.desafio_forohub.model.Topico;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;



public interface ITopicoRepository extends JpaRepository<Topico, Long> {

    boolean existsByTituloAndMensajes_contenido(String titulo, String mensaje);

    @Query("SELECT t from Topico t WHERE t.status <> 'CERRADO'")
    Page<Topico> findAllActive(Pageable pageable);

    @Query("SELECT t FROM Topico t WHERE t.curso = :curso AND t.status <> 'CERRADO'")
    Page<Topico> findByCursoAndStatusNotCloset(@Param("curso") Curso curso, Pageable pageable);
}
