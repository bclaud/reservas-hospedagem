package com.bclaud.reservas.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.bclaud.reservas.domain.Anuncio;
import com.bclaud.reservas.domain.CaracteristicaImovel;
import com.bclaud.reservas.domain.Endereco;
import com.bclaud.reservas.domain.FormaPagamento;
import com.bclaud.reservas.domain.Imovel;
import com.bclaud.reservas.domain.TipoAnuncio;
import com.bclaud.reservas.domain.TipoImovel;
import com.bclaud.reservas.domain.Usuario;
import com.bclaud.reservas.exceptions.RecursoNaoEncontradoException;
import com.bclaud.reservas.exceptions.imovel.ImovelExceptions;
import com.bclaud.reservas.repositories.ImovelRepository;
import com.bclaud.reservas.services.dto.imovel.CadastrarImovelRequest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
public class ImovelServiceTest {

    @Captor
    ArgumentCaptor<Imovel> imovelCaptor;

    @Mock
    UsuarioService usuarioService;

    @Mock
    AnuncioService anuncioService;

    @Mock
    ImovelRepository imovelRepository;

    @InjectMocks
    ImovelService imovelService;

    Imovel imovel = new Imovel();
    List<Imovel> listaImovel = new ArrayList<Imovel>();

    Usuario usuario = new Usuario();
    Endereco endereco = new Endereco();
    CadastrarImovelRequest imovelRequest = new CadastrarImovelRequest();
    Pageable page;

    Anuncio anuncio = new Anuncio();
    @BeforeEach
    public void init() {
        List<Imovel> listaImovel = new ArrayList<Imovel>();
        listaImovel.add(imovel);

        List<CaracteristicaImovel> listaCaracteristicas = new ArrayList<>();
        listaCaracteristicas.add(new CaracteristicaImovel(1L, "praia"));
        listaCaracteristicas.add(new CaracteristicaImovel(2L, "Teste"));

        imovel = Imovel.builder().id(1L).identificacao("identificacao teste").endereco(endereco).proprietario(usuario)
                .tipoImovel(TipoImovel.HOTEL)
                .caracteristicas(listaCaracteristicas)
                .build();

        usuario = Usuario.builder().id(1L).nome("test user").senha("123456").cpf("12345678912")
                .dataNascimento(LocalDate.of(1990, Month.JANUARY, 1)).endereco(endereco)
                .avatarURL("https://i.some-random-api.ml/fh0zJdniX4.jpg").build();

        page = PageRequest.of(0, 10);

        endereco = Endereco.builder().id(1L).cep("12345-123").logradouro("logradouro teste").numero("111")
        .complemento("complemento teste").bairro("bairro teste").cidade("cidade teste").estado("estado teste")
        .build();

        imovelRequest = CadastrarImovelRequest.builder().identificacao("identificacao teste")
                .tipoImovel(TipoImovel.HOTEL).endereco(endereco).idProprietario(1L)
                .caracteristicas(listaCaracteristicas)
                .build();

    }

    @Test
    void buscarImovelPorId_RetornaImovelDoID() {
        when(imovelRepository.findById(1L)).thenReturn(Optional.of(imovel));

        assertEquals(imovel, imovelService.buscarImovelPorId(1L));
    }

    @Test
    void buscarImovelPorId_RetornaExcecaoNaoEncontrado() {
        assertThrows(RecursoNaoEncontradoException.class,() -> imovelService.buscarImovelPorId(1L));
    }

    @Test
    void cadastrarImovel_RetornaImovelCadastrado() {
        when(imovelRepository.save(any(Imovel.class))).thenReturn(imovel);

        assertEquals(imovel, imovelService.cadastrarImovel(imovelRequest));
    }

    @Test
    void listarImoveis_RetornaPageComConteudo() {
        listaImovel.add(imovel);
        when(imovelRepository.findAll(page)).thenReturn(new PageImpl<Imovel>(listaImovel, page, 1));

        assertTrue(imovelService.listarImoveis(page).hasContent());
    }

    @Test
    void listarImoveisPorProprietario_RetornaPageComConteudo() {
        listaImovel.add(imovel);
        when(imovelRepository.findByProprietario_Id(1L, page)).thenReturn(new PageImpl<Imovel>(listaImovel, page, 1));

        assertTrue(imovelService.listarImoveisPorProprietario(1L, page).hasContent());
    }

    @Test
    void deletarImovelPorId_RetornaExcecaoNaoEncontrado(){
        assertThrows(RecursoNaoEncontradoException.class, () -> imovelService.deletarImovelPorId(1L));
    }

    @Test
    void deletarImovelPorId_RetornaExcecaoAnuncioVinculado(){
        List<FormaPagamento> listaFormasPagamento = new ArrayList<>();
        listaFormasPagamento.add(FormaPagamento.PIX);
        listaFormasPagamento.add(FormaPagamento.CARTAO_CREDITO);
        anuncio = Anuncio.builder().id(1L).tipoAnuncio(TipoAnuncio.COMPLETO).imovel(imovel).anunciante(usuario)
        .valorDiaria(BigDecimal.valueOf(50L))
        .formasAceitas(listaFormasPagamento)
        .descricao("descricao test").build();

        List<Anuncio> listaAnuncio = new ArrayList<>();
        listaAnuncio.add(anuncio);


        
        when(anuncioService.findAll()).thenReturn(listaAnuncio);
        
        assertThrows(ImovelExceptions.class, () -> imovelService.deletarImovelPorId(1L));
    }
}
