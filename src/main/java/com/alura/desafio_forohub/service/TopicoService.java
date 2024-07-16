package com.alura.desafio_forohub.service;


import com.alura.desafio_forohub.controller.TopicoController;
import com.alura.desafio_forohub.dto.*;
import com.alura.desafio_forohub.model.Curso;
import com.alura.desafio_forohub.model.Mensaje;
import com.alura.desafio_forohub.model.Topico;
import com.alura.desafio_forohub.repository.IMensajeRepository;
import com.alura.desafio_forohub.repository.ITopicoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;


@Service
public class TopicoService {

    @Autowired
    private ITopicoRepository iTopicoRepository;

    @Autowired
    private IMensajeRepository iMensajeRepository;


    public Topico registrarTopico (DatosRegistroTopico datosRegistroTopico){

        if(iTopicoRepository.existsByTituloAndMensajes_contenido(datosRegistroTopico.titulo(), datosRegistroTopico.mensaje())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El tópico ya existe");
        }

        Topico nuevoTopico = new Topico(datosRegistroTopico);
        return iTopicoRepository.save(nuevoTopico);
    }

    public Page<DatosListadoTopico> listarTopicos(Pageable paginacion){
        return iTopicoRepository.findAllActive(paginacion).map(DatosListadoTopico::new);
    }

    public PagedModel<EntityModel<DatosListadoTopico>> convertirAPagedModel(Page<DatosListadoTopico> topicosPage,
                                                                            PagedResourcesAssembler<DatosListadoTopico> pagedResourcesAssembler,
                                                                            Pageable paginacion){
        return pagedResourcesAssembler.toModel(topicosPage,
                topico-> EntityModel.of(topico,
                        WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(TopicoController.class)
                                .listadoTopicos(paginacion)).withSelfRel()));
    }

    public Page<DatosListadoTopico> buscarTopicosPorCurso(String nombreCurso, Pageable paginacion){
        Curso curso;
        try{
            curso = Curso.valueOf(nombreCurso.toLowerCase());

        }catch (IllegalArgumentException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Curso inválido");

        }
        return iTopicoRepository.findByCursoAndStatusNotCloset(curso, paginacion).map(DatosListadoTopico::new);
    }

    public Optional<Topico> buscarTopicoPorId(Long id){
        return iTopicoRepository.findById(id);
    }


    public void actualizarTopico(Long id, DatosActualizarTopico datosActualizarTopico){
        Topico topico = iTopicoRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tópico no encontrado"));

        topico.actualizarTopico(datosActualizarTopico);

        iTopicoRepository.save(topico);

    }

    public DatosListadoMensaje obtenerUltimoMensaje(Long id){

        Topico topico = iTopicoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tópico no encontrado"));

        List<Mensaje> mensajes = topico.getMensajes();
        if(!mensajes.isEmpty()){
            Mensaje ultimoMensaje = mensajes.get(mensajes.size() - 1);
            return new DatosListadoMensaje(
                    ultimoMensaje.getId(),
                    ultimoMensaje.getContenido(),
                    ultimoMensaje.getFecha(),
                    ultimoMensaje.getAutor()
            );

        }else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No hay mensajes en el tópico");
        }

    }


    public DatosListadoMensaje agregarMensaje(Long id, DatosNuevoMensaje datosNuevoMensaje){

        Topico topico = iTopicoRepository.findById(id)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tópico no encontrado"));

        Mensaje nuevoMensaje = new Mensaje(datosNuevoMensaje);
        topico.cerrarTopico();

        iTopicoRepository.save(topico);
        return new DatosListadoMensaje(nuevoMensaje);
    }

    @Transactional
    public void cerrarTopico(Long id){

        Topico topico = iTopicoRepository.findById(id)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tópico no encontrado"));

        topico.cerrarTopico();

        iTopicoRepository.save(topico);
    }

   public void eliminarMensaje (Long idTopico, Long idMensaje){

        Topico topico = iTopicoRepository.findById(idTopico)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tópico no encontrado"));

        Mensaje mensaje = topico.getMensajes().stream()
                .filter(m->m.getId().equals(idMensaje))
                .findFirst()
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Mensaje no encontrado"));

        topico.getMensajes().remove(mensaje);

        iTopicoRepository.save(topico);

        iMensajeRepository.deleteById(idMensaje);
   }
}
