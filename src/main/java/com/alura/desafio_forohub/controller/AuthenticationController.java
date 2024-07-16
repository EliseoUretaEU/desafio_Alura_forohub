package com.alura.desafio_forohub.controller;


import com.alura.desafio_forohub.dto.DatosAutenticacionUsuario;
import com.alura.desafio_forohub.infra.security.DatosJWTToken;
import com.alura.desafio_forohub.infra.security.TokenService;
import com.alura.desafio_forohub.model.Usuario;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.security.sasl.AuthenticationException;

@RestController
@RequestMapping("/login")
@Tag(name = "Authentication", description = "Endpoints para autenticación de usuarios ya registrados y gestión de tokens JWT.")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    /**
     * Autentica a un usuario y genera un token JWT.
     *
     * @param datosAutenticacionUsuario Datos de autenticación del usuario.
     * @return ResponseEntity con el token JWT o un mensaje de error.
     */
    @PostMapping()
    @Operation(summary = "Autenticar usuario",
            description = "Autentica a un usuario con sus credenciales y genera un token JWT.")
    public ResponseEntity<?> autenticarUsuario(
            @Parameter(description = "Datos de autenticación del usuario", required = true)
            @RequestBody @Valid DatosAutenticacionUsuario datosAutenticacionUsuario) throws AuthenticationException {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                datosAutenticacionUsuario.email(),
                datosAutenticacionUsuario.clave()
        );
        var usuarioAutenticado = authenticationManager.authenticate(authenticationToken);
        var JWTtoken = tokenService.generarToken((Usuario) usuarioAutenticado.getPrincipal());
        return ResponseEntity.ok(new DatosJWTToken(JWTtoken));
    }
}
