package com.bclaud.reservas.services;

import java.util.List;

import com.bclaud.reservas.domain.Anuncio;
import com.bclaud.reservas.exceptions.RecursoNaoEncontradoException;
import com.bclaud.reservas.exceptions.anuncio.AnuncioExceptions;
import com.bclaud.reservas.repositories.AnuncioRepository;
import com.bclaud.reservas.services.dto.anuncio.CadastrarAnuncioRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class AnuncioService {

    @Autowired
    AnuncioRepository anuncioRepository;

    @Autowired
    ImovelService imovelService;

    @Autowired
    UsuarioService usuarioService;

    public List<Anuncio> findAll() {
        return anuncioRepository.findAll();
    }

    public Anuncio anunciarAnuncio(CadastrarAnuncioRequest request) throws AnuncioExceptions {
        if (imovelDuplicado(request.getIdImovel())) {
            throw new AnuncioExceptions(
                    "Já existe um recurso do tipo Anuncio com IdImovel com o valor '" + request.getIdImovel() + "'.");
        }

        return anuncioRepository.save(requestToAnuncio(request));
    }

    public Page<Anuncio> listarAnuncios(Pageable page) {
        return anuncioRepository.findAll(page);
    }

    public Page<Anuncio> listarAnunciosPorAnunciante(Long id, Pageable page) {
        return anuncioRepository.findByAnunciante_id(id, page);
    }

    public void excluirAnuncio(long id) {
        listarAnuncioPorId(id);
        anuncioRepository.softDelete(id);
    }

    public Anuncio listarAnuncioPorId(Long id) throws RecursoNaoEncontradoException {
        return anuncioRepository.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException(
                "Nenhum(a) Anuncio com Id com o valor '" + id + "' foi encontrado."));
    }

    private boolean imovelDuplicado(Long idImovel) {
        return findAll().stream().anyMatch(a -> a.getImovel().getId().equals(idImovel));
    }

    private Anuncio requestToAnuncio(CadastrarAnuncioRequest request) {
        return Anuncio.builder().tipoAnuncio(request.getTipoAnuncio())
                .imovel(imovelService.buscarImovelPorId(request.getIdImovel()))
                .anunciante(usuarioService.listarUsuarioPorId(request.getIdAnunciante()))
                .valorDiaria(request.getValorDiaria()).formasAceitas(request.getFormasAceitas())
                .descricao(request.getDescricao()).build();
    }
}
