package com.bclaud.reservas.services;

import com.bclaud.reservas.domain.Imovel;
import com.bclaud.reservas.exceptions.RecursoNaoEncontradoException;
import com.bclaud.reservas.exceptions.imovel.ImovelExceptions;
import com.bclaud.reservas.repositories.ImovelRepository;
import com.bclaud.reservas.services.dto.imovel.CadastrarImovelRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ImovelService {

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    AnuncioService anuncioService;

    @Autowired
    ImovelRepository imovelRepository;

    public Imovel cadastrarImovel(CadastrarImovelRequest request) {
        Imovel imovel = requestToImovel(request);
        return imovelRepository.save(imovel);
    }

    public Page<Imovel> listarImoveis(Pageable page) {
        return imovelRepository.findAll(page);
    }

    public Page<Imovel> listarImoveisPorProprietario(Long id, Pageable page) {
        return imovelRepository.findByProprietario_Id(id, page);
    }

    public Imovel buscarImovelPorId(Long id) throws RecursoNaoEncontradoException {
        return imovelRepository.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException(
                "Nenhum(a) Imovel com Id com o valor '" + id + "' foi encontrado."));
    }

    public void deletarImovelPorId(long id) throws ImovelExceptions {
        if (possuiAnuncioPorId(id)) {
            throw new ImovelExceptions("Não é possível excluir um imóvel que possua um anúncio.");
        }
        buscarImovelPorId(id);
        imovelRepository.softDelete(id);
    }

    private Imovel requestToImovel(CadastrarImovelRequest request) {
        return Imovel.builder().identificacao(request.getIdentificacao()).tipoImovel(request.getTipoImovel())
                .endereco(request.getEndereco())
                .proprietario(usuarioService.listarUsuarioPorId(request.getIdProprietario()))
                .caracteristicas(request.getCaracteristicas()).build();
    }

    private boolean possuiAnuncioPorId(Long id) {
        return anuncioService.findAll().stream().anyMatch(a -> a.getImovel().getId().equals(id));
    }
}
