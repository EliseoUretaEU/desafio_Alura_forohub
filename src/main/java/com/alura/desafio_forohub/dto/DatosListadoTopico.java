package com.alura.desafio_forohub.dto;

import com.alura.desafio_forohub.model.Curso;
import com.alura.desafio_forohub.model.Topico;

import java.util.List;
import java.util.stream.Collectors;

public record DatosListadoTopico(
        Long id,
        String titulo,
        List<DatosListadoMensaje> mensajes,
        String status,
        Curso curso

) {

    public DatosListadoTopico(Topico topico){

        this(topico.getId(),
                topico.getTitulo(),
                topico.getMensajes().stream()
                        .map(DatosListadoMensaje::new)
                        .collect(Collectors.toList()),
                topico.getStatus(),
                topico.getCurso());
    }
}
