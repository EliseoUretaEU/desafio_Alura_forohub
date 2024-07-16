package com.alura.desafio_forohub.dto;

import com.alura.desafio_forohub.model.Mensaje;

import java.time.LocalDateTime;

public record DatosListadoMensaje(
        Long id,
        String contenido,
        LocalDateTime fecha,
        String autor
) {

    public DatosListadoMensaje(Mensaje mensaje){
        this(mensaje.getId(),
                mensaje.getContenido(),
                mensaje.getFecha(),
                mensaje.getAutor());

    }

    public DatosListadoMensaje(Long id, String contenido, LocalDateTime fecha, String autor){
        this.id = id;
        this.contenido = contenido;
        this.fecha = fecha;
        this.autor = autor;

    }
}
