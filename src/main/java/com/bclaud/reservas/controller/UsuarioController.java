package com.bclaud.reservas.controller;

import javax.validation.Valid;

import com.bclaud.reservas.domain.Usuario;
import com.bclaud.reservas.services.UsuarioService;
import com.bclaud.reservas.services.dto.usuario.AtualizarUsuarioRequest;
import com.bclaud.reservas.services.dto.usuario.UsuarioRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
    
    @Autowired
    UsuarioService usuarioService;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Usuario cadastrarUsuario(@RequestBody @Valid UsuarioRequest request){
        return usuarioService.cadastrarUsuario(request);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<Usuario> listarUsuarios(@PageableDefault(sort = "nome", direction = Direction.ASC) @ApiIgnore Pageable page){
        return usuarioService.listarUsuarios(page);
    }

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public Usuario listarUsuarioPorId(@PathVariable long id){
        return usuarioService.listarUsuarioPorId(id);
    }

    @GetMapping("/cpf/{cpf}")
    @ResponseStatus(HttpStatus.OK)
    public Usuario listarUsuarioPorCpf(@PathVariable String cpf){
        return usuarioService.listarUsuarioPorCPF(cpf);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Usuario atualizarUsuario(@PathVariable Long id, @RequestBody @Valid AtualizarUsuarioRequest request){
        return usuarioService.atualizarUsuario(id, request);
    }
}
