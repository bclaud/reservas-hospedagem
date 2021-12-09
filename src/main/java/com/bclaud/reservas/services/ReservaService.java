package com.bclaud.reservas.services;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.bclaud.reservas.domain.FormaPagamento;
import com.bclaud.reservas.domain.Pagamento;
import com.bclaud.reservas.domain.Periodo;
import com.bclaud.reservas.domain.Reserva;
import com.bclaud.reservas.domain.StatusPagamento;
import com.bclaud.reservas.domain.TipoImovel;
import com.bclaud.reservas.exceptions.RecursoNaoEncontradoException;
import com.bclaud.reservas.exceptions.reserva.QuantidadeDeDiariasExceptions;
import com.bclaud.reservas.exceptions.reserva.QuantidadeDePessoasExceptions;
import com.bclaud.reservas.exceptions.reserva.ReservaExceptions;
import com.bclaud.reservas.repositories.ReservaRepository;
import com.bclaud.reservas.services.dto.reserva.CadastrarReservaRequest;
import com.bclaud.reservas.services.dto.reserva.InformacaoReservaResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ReservaService {

    @Autowired
    ReservaRepository reservaRepository;

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    AnuncioService anuncioService;

    @Autowired
    PeriodoService periodoService;

    @Autowired
    PagamentoService pagamentoService;

    public InformacaoReservaResponse realizarReserva(CadastrarReservaRequest request) throws ReservaExceptions {
        Periodo periodoValidado = validarPeriodo(request.getPeriodo());

        Reserva reserva = requestToReserva(request);
        reserva.setPeriodo(periodoValidado);
        validarQuantidadesMinimas(reserva);

        if (!anuncianteValido(reserva)) {
            throw new ReservaExceptions("O solicitante de uma reserva não pode ser o próprio anunciante.");
        }

        if (!periodoDisponivel(reserva)) {
            throw new ReservaExceptions("Este anuncio já esta reservado para o período informado.");
        }

        reserva.setDataHoraReserva(LocalDateTime.now());

        Integer quantidadeDias = periodoService.calcularQuantidadeDiarias(request.getPeriodo());
        BigDecimal valorDiaria = reserva.getAnuncio().getValorDiaria();

        reserva.getPagamento().setValorTotal(pagamentoService.calcularValorTotalDiarias(quantidadeDias, valorDiaria));

        reservaRepository.save(reserva);
        return InformacaoReservaResponse.of(reserva);
    }

    public Page<Reserva> listarReservaPorSolicitante(Long idSolicitante, Pageable page) {
        return reservaRepository.findBySolicitante_id(idSolicitante, page);
    }

    public Page<Reserva> listarReservasPorSolicitanteComFiltro(Long idSolicitante, String dataHoraInicial,
            String dataHoraFinal, Pageable page) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime inicio = LocalDateTime.parse(dataHoraInicial, formatter);
        LocalDateTime fim = LocalDateTime.parse(dataHoraFinal, formatter);

        return reservaRepository
                .findBySolicitanteIdAndPeriodoDataHoraInicialGreaterThanEqualAndPeriodoDataHoraFinalLessThanEqual(
                        idSolicitante, inicio, fim, page);
    }

    public Page<Reserva> listarReservaPorAnunciante(Long idAnunciante, Pageable page) {
        return reservaRepository.findByAnuncio_Anunciante_Id(idAnunciante, page);
    }

    public Reserva findReservaById(Long id) throws RecursoNaoEncontradoException {
        return reservaRepository.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException(
                "Nenhum(a) Reserva com Id com o valor '" + id + "' foi encontrado."));
    }

    public void pagarReserva(Long idReserva, FormaPagamento formaPagamento) throws ReservaExceptions {
        Reserva reserva = findReservaById(idReserva);

        if (!reservaAceitaFormaDePagamento(reserva, formaPagamento)) {
            throw new ReservaExceptions("O anúncio não aceita " + formaPagamento.toString()
                    + " como forma de pagamento. As formas aceitas são " + formasDePagamentoAceitas(reserva));
        }

        if (!reserva.estaPagamentoPendente()) {
            throw new ReservaExceptions(
                    "Não é possível realizar o pagamento para esta reserva, pois ela não está no status PENDENTE.");
        }

        reserva.getPagamento().setStatus(StatusPagamento.PAGO);
        reserva.getPagamento().setFormaEscolhida(formaPagamento);
        reservaRepository.save(reserva);
    }

    public void cancelarReserva(Long idReserva) throws ReservaExceptions {
        Reserva reserva = findReservaById(idReserva);

        if (!reserva.estaPagamentoPendente()) {
            throw new ReservaExceptions(
                    "Não é possível realizar o cancelamento para esta reserva, pois ela não está no status PENDENTE.");
        }

        reserva.getPagamento().setStatus(StatusPagamento.CANCELADO);
        reservaRepository.save(reserva);
    }

    public void estornarReserva(Long idReserva) throws ReservaExceptions {
        Reserva reserva = findReservaById(idReserva);

        if (!reserva.estaPago()) {
            throw new ReservaExceptions(
                    "Não é possível realizar o estorno para esta reserva, pois ela não está no status PAGO.");
        }

        reserva.getPagamento().setStatus(StatusPagamento.ESTORNADO);
        reservaRepository.save(reserva);
    }

    private String formasDePagamentoAceitas(Reserva reserva) {
        List<FormaPagamento> listaFormasPagamento = reserva.getAnuncio().getFormasAceitas();

        StringBuilder formasAceitas = new StringBuilder();
        for (int i = 0; i < listaFormasPagamento.size(); i++) {
            if (i != listaFormasPagamento.size() - 1) {
                formasAceitas.append(listaFormasPagamento.get(i) + ", ");
            } else {
                formasAceitas.append(listaFormasPagamento.get(i) + ".");
            }
        }

        return formasAceitas.toString();
    }

    private boolean reservaAceitaFormaDePagamento(Reserva reserva, FormaPagamento formaPagamento) {
        return reserva.getAnuncio().getFormasAceitas().stream().anyMatch(f -> f.equals(formaPagamento));
    }

    private Periodo validarPeriodo(Periodo periodo) {
        periodoService.checkPeriodo(periodo);
        final int HORARIOINICIOOBRIGATORIO = 14;
        final int HORARIOFINALOBRIGATORIO = 12;

        if (periodo.getDataHoraInicial().getHour() == HORARIOINICIOOBRIGATORIO
                && periodo.getDataHoraFinal().getHour() == HORARIOFINALOBRIGATORIO) {
            return periodo;
        }

        return Periodo.builder()
                .dataHoraInicial(LocalDateTime.of(periodo.getDataHoraInicial().getYear(),
                        periodo.getDataHoraInicial().getMonth(), periodo.getDataHoraInicial().getDayOfMonth(),
                        HORARIOINICIOOBRIGATORIO, 00))
                .dataHoraFinal(
                        (LocalDateTime.of(periodo.getDataHoraFinal().getYear(), periodo.getDataHoraFinal().getMonth(),
                                periodo.getDataHoraFinal().getDayOfMonth(), HORARIOFINALOBRIGATORIO, 00)))
                .build();
    }

    private boolean periodoDisponivel(Reserva reserva) {
        LocalDateTime dataInicio = reserva.getPeriodo().getDataHoraInicial();
        LocalDateTime dataFinal = reserva.getPeriodo().getDataHoraFinal();
        List<Reserva> reservasNoPeriodo = reservaRepository
                .findByAnuncioIdAndPeriodoDataHoraInicialLessThanEqualAndPeriodoDataHoraFinalGreaterThanEqual(
                        reserva.getAnuncio().getId(), dataFinal, dataInicio);
        return reservasNoPeriodo.stream().noneMatch(r -> r.estaAtiva());
    }

    private Reserva requestToReserva(CadastrarReservaRequest request) {
        return Reserva.builder().solicitante(usuarioService.listarUsuarioPorId(request.getIdSolicitante()))
                .anuncio(anuncioService.listarAnuncioPorId(request.getIdAnuncio())).periodo(request.getPeriodo())
                .quantidadePessoas(request.getQuantidadePessoas()).pagamento(pagamentoPendentePadrao()).build();
    }

    private boolean anuncianteValido(Reserva reserva) {
        return !reserva.getSolicitante().getId().equals(reserva.getAnuncio().getAnunciante().getId());
    }

    private Pagamento pagamentoPendentePadrao() {
        return new Pagamento(null, null, StatusPagamento.PENDENTE);
    }

    private void validarQuantidadesMinimas(Reserva reserva)
            throws QuantidadeDePessoasExceptions, QuantidadeDeDiariasExceptions {
        final Integer MINIMOPESSOASHOTEL = 2;
        final Integer MINIMODIARIASPOUSADA = 5;

        Integer quantidadePessoas = reserva.getQuantidadePessoas();
        TipoImovel tipoImovel = reserva.getAnuncio().getImovel().getTipoImovel();
        Integer quantidadeDiarias = periodoService.calcularQuantidadeDiarias(reserva.getPeriodo());

        if (tipoImovel.equals(TipoImovel.HOTEL) && quantidadePessoas < MINIMOPESSOASHOTEL) {
            throw new QuantidadeDePessoasExceptions(MINIMOPESSOASHOTEL.toString(), tipoImovel.toString());
        }

        if (tipoImovel.equals(TipoImovel.POUSADA) && quantidadeDiarias < MINIMODIARIASPOUSADA) {
            throw new QuantidadeDeDiariasExceptions(MINIMODIARIASPOUSADA.toString(), tipoImovel.toString());
        }
    }
}
