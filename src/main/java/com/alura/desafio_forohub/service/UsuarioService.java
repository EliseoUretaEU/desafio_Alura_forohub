package com.alura.desafio_forohub.service;

import com.alura.desafio_forohub.dto.DatosNuevoUsuario;
import com.alura.desafio_forohub.model.Usuario;
import com.alura.desafio_forohub.repository.IUsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    @Autowired
    private IUsuarioRepository iUsuarioRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public Usuario registrarNuevoUsuario(DatosNuevoUsuario datosNuevoUsuario){

        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNombre(datosNuevoUsuario.nombre());
        nuevoUsuario.setEmail(datosNuevoUsuario.email());
        nuevoUsuario.setClave(passwordEncoder.encode(datosNuevoUsuario.clave()));

        return iUsuarioRepository.save(nuevoUsuario);

    }
}
