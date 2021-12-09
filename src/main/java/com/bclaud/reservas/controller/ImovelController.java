package com.bclaud.reservas.controller;

import javax.validation.Valid;

import com.bclaud.reservas.domain.Imovel;
import com.bclaud.reservas.services.ImovelService;
import com.bclaud.reservas.services.dto.imovel.CadastrarImovelRequest;

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
@RequestMapping("/imoveis")
public class ImovelController {

    @Autowired
    ImovelService imovelService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Imovel cadastrarImovel(@RequestBody @Valid CadastrarImovelRequest request) {
        return imovelService.cadastrarImovel(request);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<Imovel> listarImoveis(
            @PageableDefault(sort = "identificacao", direction = Direction.ASC) @ApiIgnore Pageable page) {
        return imovelService.listarImoveis(page);
    }

    @GetMapping(value = "/proprietarios/{idProprietario}")
    @ResponseStatus(HttpStatus.OK)
    public Page<Imovel> listarImoveisPorProprietario(@PathVariable Long idProprietario,
            @PageableDefault(sort = "identificacao", direction = Direction.ASC) @ApiIgnore Pageable page) {
        return imovelService.listarImoveisPorProprietario(idProprietario, page);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Imovel buscarImovelPorId(@PathVariable Long id){
        return imovelService.buscarImovelPorId(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deletarImovelPorId(@PathVariable long id){
        imovelService.deletarImovelPorId(id);
    }

}
