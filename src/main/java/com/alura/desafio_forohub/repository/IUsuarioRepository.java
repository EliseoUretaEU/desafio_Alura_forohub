package com.alura.desafio_forohub.repository;

import com.alura.desafio_forohub.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface IUsuarioRepository extends JpaRepository <Usuario, Long> {

    UserDetails findByEmail(String username);
}
