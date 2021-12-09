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
import com.bclaud.reservas.exceptions.anuncio.AnuncioExceptions;
import com.bclaud.reservas.repositories.AnuncioRepository;
import com.bclaud.reservas.services.dto.anuncio.CadastrarAnuncioRequest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
public class AnuncioServiceTest {

        @Mock
        AnuncioRepository anuncioRepository;

        @Mock
        ImovelService imovelService;

        @Mock
        UsuarioService usuarioService;

        @InjectMocks
        AnuncioService anuncioService;

        Usuario usuario = new Usuario();
        Endereco endereco = new Endereco();

        Anuncio anuncio = new Anuncio();
        CadastrarAnuncioRequest anuncioRequest = new CadastrarAnuncioRequest();
        List<Anuncio> listaAnuncio = new ArrayList<>();

        Imovel imovel = new Imovel();
        Pageable page;
        List<CaracteristicaImovel> listaCaracteristicas = new ArrayList<>();
        List<FormaPagamento> listaFormasPagamento = new ArrayList<>();


        @BeforeEach
        public void init() {
                listaCaracteristicas.add(new CaracteristicaImovel(1L, "praia"));
                listaCaracteristicas.add(new CaracteristicaImovel(2L, "Teste"));

                listaFormasPagamento.add(FormaPagamento.PIX);
                listaFormasPagamento.add(FormaPagamento.CARTAO_CREDITO);

                endereco = Endereco.builder().id(1L).cep("12345-123").logradouro("logradouro teste").numero("111")
                                .complemento("complemento teste").bairro("bairro teste").cidade("cidade teste")
                                .estado("estado teste").build();

                usuario = Usuario.builder().id(1L).nome("test user").email("test@test.com").senha("123456")
                                .cpf("12345678912").dataNascimento(LocalDate.of(1990, Month.JANUARY, 1))
                                .endereco(endereco).avatarURL("https://i.some-random-api.ml/fh0zJdniX4.jpg").build();

                imovel = Imovel.builder().id(1L).identificacao("identificacao teste").endereco(endereco)
                                .proprietario(usuario).tipoImovel(TipoImovel.HOTEL)
                                .caracteristicas(listaCaracteristicas)
                                .build();

                anuncioRequest = CadastrarAnuncioRequest.builder().tipoAnuncio(TipoAnuncio.COMPLETO).idImovel(1L)
                                .idAnunciante(1L).valorDiaria(BigDecimal.valueOf(50L))
                                .formasAceitas(listaFormasPagamento)
                                .descricao("descricao test").build();

                anuncio = Anuncio.builder().id(1L).tipoAnuncio(TipoAnuncio.COMPLETO).imovel(imovel).anunciante(usuario)
                                .valorDiaria(BigDecimal.valueOf(50L))
                                .formasAceitas(listaFormasPagamento)
                                .descricao("descricao test").build();

                listaAnuncio.add(anuncio);

                page = PageRequest.of(0, 10);
        }

        @Test
        void anunciarAnuncio_RetornaAnuncioCriado() {
                when(anuncioRepository.findAll()).thenReturn(new ArrayList<Anuncio>());
                when(anuncioRepository.save(any(Anuncio.class))).thenReturn(anuncio);

                when(imovelService.buscarImovelPorId(1L)).thenReturn(imovel);

                when(usuarioService.listarUsuarioPorId(1L)).thenReturn(usuario);

                assertEquals(anuncio.getId(), anuncioService.anunciarAnuncio(anuncioRequest).getId());
        }

        @Test
        void anunciarAnuncio_RetornaExcecaoAnuncioDuplicado() {
                when(anuncioRepository.findAll()).thenReturn(listaAnuncio);

                assertThrows(AnuncioExceptions.class, () -> anuncioService.anunciarAnuncio(anuncioRequest));
        }

        @Test
        void anunciarAnuncio_ImovelDuplicado() {
                when(anuncioRepository.findAll()).thenReturn(listaAnuncio);

                assertThrows(AnuncioExceptions.class, () -> anuncioService.anunciarAnuncio(anuncioRequest));
        }

        @Test
        void findAll_RetornaListaDeAnuncio() {
                when(anuncioRepository.findAll()).thenReturn(listaAnuncio);

                assertEquals(1, anuncioService.findAll().size());
        }

        @Test
        void listarAnuncioPorId_RetornaAnuncioDoID() {
                when(anuncioRepository.findById(1L)).thenReturn(Optional.of(anuncio));

                assertEquals(anuncio.getId(), anuncioService.listarAnuncioPorId(1L).getId());
        }

        @Test
        void listarAnuncioPorId_RetornaExcecaoNaoEncontrado() {
                assertThrows(RecursoNaoEncontradoException.class, () -> anuncioService.listarAnuncioPorId(1L));
        }

        @Test
        void listarAnuncios_RetornaPageComConteudo() {
                when(anuncioRepository.findAll(page)).thenReturn(new PageImpl<Anuncio>(listaAnuncio, page, 1));

                assertTrue(anuncioService.listarAnuncios(page).hasContent());
        }
}
