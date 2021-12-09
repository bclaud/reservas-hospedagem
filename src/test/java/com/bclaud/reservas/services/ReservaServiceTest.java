package com.bclaud.reservas.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.bclaud.reservas.domain.Anuncio;
import com.bclaud.reservas.domain.CaracteristicaImovel;
import com.bclaud.reservas.domain.Endereco;
import com.bclaud.reservas.domain.FormaPagamento;
import com.bclaud.reservas.domain.Imovel;
import com.bclaud.reservas.domain.Pagamento;
import com.bclaud.reservas.domain.Periodo;
import com.bclaud.reservas.domain.Reserva;
import com.bclaud.reservas.domain.StatusPagamento;
import com.bclaud.reservas.domain.TipoAnuncio;
import com.bclaud.reservas.domain.TipoImovel;
import com.bclaud.reservas.domain.Usuario;
import com.bclaud.reservas.exceptions.RecursoNaoEncontradoException;
import com.bclaud.reservas.exceptions.reserva.ReservaExceptions;
import com.bclaud.reservas.repositories.ReservaRepository;
import com.bclaud.reservas.services.dto.anuncio.CadastrarAnuncioRequest;
import com.bclaud.reservas.services.dto.reserva.CadastrarReservaRequest;
import com.bclaud.reservas.services.dto.reserva.DadosAnuncioResponse;
import com.bclaud.reservas.services.dto.reserva.DadosSolicitanteResponse;
import com.bclaud.reservas.services.dto.reserva.InformacaoReservaResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
public class ReservaServiceTest {

        @Mock
        ReservaRepository reservaRepository;

        @Mock
        UsuarioService usuarioService;

        @Mock
        AnuncioService anuncioService;

        @Mock
        PeriodoService periodoService;

        @Mock
        PagamentoService pagamentoService;

        @InjectMocks
        ReservaService reservaService;

        @Captor
        ArgumentCaptor<Reserva> reservaArgumentCaptor;

        Usuario usuario = new Usuario();
        Usuario usuario2 = new Usuario();
        Endereco endereco = new Endereco();

        Anuncio anuncio = new Anuncio();
        CadastrarAnuncioRequest anuncioRequest = new CadastrarAnuncioRequest();

        Imovel imovel = new Imovel();
        Pageable page;

        CadastrarReservaRequest reservaRequest = new CadastrarReservaRequest();
        InformacaoReservaResponse reservaResponse = new InformacaoReservaResponse();
        Reserva reserva = new Reserva();

        Periodo periodo = new Periodo();
        DadosSolicitanteResponse dadosSolicitantesResponse = new DadosSolicitanteResponse();
        DadosAnuncioResponse dadosAnuncioResponse = new DadosAnuncioResponse();

        Pagamento pagamento = new Pagamento();

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

                usuario2 = Usuario.builder().id(2L).nome("test user2").email("test@test2.com").senha("123456")
                                .cpf("12345278912").dataNascimento(LocalDate.of(1990, Month.JANUARY, 1))
                                .endereco(endereco).avatarURL("https://i.some-random-api.ml/fh0zJdniX4.jpg").build();

                imovel = Imovel.builder().id(1L).identificacao("identificacao teste").endereco(endereco)
                                .proprietario(usuario).tipoImovel(TipoImovel.HOTEL)
                                .caracteristicas(listaCaracteristicas)
                                .build();

                anuncioRequest = CadastrarAnuncioRequest.builder().tipoAnuncio(TipoAnuncio.COMPLETO).idImovel(1L)
                                .idAnunciante(1L).valorDiaria(BigDecimal.valueOf(50L))
                                .formasAceitas(listaFormasPagamento)
                                .descricao("descricao test").build();

                anuncio = Anuncio.builder().id(1L).tipoAnuncio(TipoAnuncio.COMPLETO).imovel(imovel).anunciante(usuario2)
                                .valorDiaria(BigDecimal.valueOf(50L))
                                .formasAceitas(listaFormasPagamento)
                                .descricao("descricao test").build();

                page = PageRequest.of(0, 10);

                dadosSolicitantesResponse = DadosSolicitanteResponse.of(usuario);
                dadosAnuncioResponse = DadosAnuncioResponse.of(anuncio);
                pagamento = new Pagamento(null, null, StatusPagamento.PENDENTE);

                reservaRequest.setIdSolicitante(1L);
                reservaRequest.setIdAnuncio(1L);
                reservaRequest.setPeriodo(periodo);
                reservaRequest.setQuantidadePessoas(2);

                reservaResponse = InformacaoReservaResponse.builder().id(1L).solicitante(dadosSolicitantesResponse)
                                .anuncio(dadosAnuncioResponse).quantidadePessoas(2).periodo(periodo)
                                .pagamento(pagamento).build();

                reserva = Reserva.builder().id(1L).solicitante(usuario).anuncio(anuncio).periodo(periodo)
                                .quantidadePessoas(2).dataHoraReserva(LocalDateTime.now()).pagamento(pagamento).build();

                periodo = Periodo.builder().dataHoraInicial(LocalDateTime.of(2021, Month.JANUARY, 1, 14, 0, 0))
                                .dataHoraFinal(LocalDateTime.of(20211, Month.JANUARY, 1, 12, 0, 0)).build();

        }

        @Test
        void findReservaById_RetornaReservaDoID() {
                when(reservaRepository.findById(1L)).thenReturn(Optional.of(reserva));

                assertEquals(reserva.getId(), reservaService.findReservaById(1L).getId());
        }

        @Test
        void findReservaById_RetornaExcecaoNaoEncontrado() {
                assertThrows(RecursoNaoEncontradoException.class, () -> reservaService.findReservaById(1L));
        }

        @Test
        void pagarReserva_RetornaExcecaoFormaPagamento() {
                when(reservaRepository.findById(1L)).thenReturn(Optional.of(reserva));

                assertThrows(ReservaExceptions.class, () -> reservaService.pagarReserva(1L, FormaPagamento.CARTAO_DEBITO));
        }

        @Test
        void pagarReserva_RetornaExcecaoStatusPagamento() {
                reserva.getPagamento().setStatus(StatusPagamento.CANCELADO);
                when(reservaRepository.findById(1L)).thenReturn(Optional.of(reserva));

                assertThrows(ReservaExceptions.class, () -> reservaService.pagarReserva(1L, FormaPagamento.PIX));
        }

        @Test
        void estornarReserva_RetornaExcecaoStatusPagamento() {
                reserva.getPagamento().setStatus(StatusPagamento.CANCELADO);
                when(reservaRepository.findById(1L)).thenReturn(Optional.of(reserva));

                assertThrows(ReservaExceptions.class, () -> reservaService.estornarReserva(1L));
        }
}
