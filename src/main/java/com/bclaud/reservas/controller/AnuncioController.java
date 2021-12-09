package com.bclaud.reservas.controller;

import javax.validation.Valid;

import com.bclaud.reservas.domain.Anuncio;
import com.bclaud.reservas.services.AnuncioService;
import com.bclaud.reservas.services.dto.anuncio.CadastrarAnuncioRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("/anuncios")
public class AnuncioController {

    @Autowired
    AnuncioService anuncioService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Anuncio anunciarAnuncio(@RequestBody @Valid CadastrarAnuncioRequest request) {
        return anuncioService.anunciarAnuncio(request);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<Anuncio> listarAnuncios(@PageableDefault(sort = "valorDiaria", direction = Direction.ASC) @ApiIgnore Pageable page){
        return anuncioService.listarAnuncios(page);
    }

    @GetMapping("/anunciantes/{anuncianteId}")
    @ResponseStatus(HttpStatus.OK)
    public Page<Anuncio> listarAnunciosPorAnunciante(@PathVariable Long anuncianteId,@PageableDefault(sort = "valorDiaria", direction = Direction.ASC) @ApiIgnore Pageable page){
        return anuncioService.listarAnunciosPorAnunciante(anuncianteId, page);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void excluirAnuncio(@PathVariable long id){
        anuncioService.excluirAnuncio(id);
    }
    
}
