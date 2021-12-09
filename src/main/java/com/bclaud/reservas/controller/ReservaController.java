package com.bclaud.reservas.controller;

import javax.validation.Valid;

import com.bclaud.reservas.domain.FormaPagamento;
import com.bclaud.reservas.domain.Reserva;
import com.bclaud.reservas.services.ReservaService;
import com.bclaud.reservas.services.dto.reserva.CadastrarReservaRequest;
import com.bclaud.reservas.services.dto.reserva.InformacaoReservaResponse;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("/reservas")
public class ReservaController {

    @Autowired
    ReservaService reservaService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public InformacaoReservaResponse realizarReserva(@RequestBody @Valid CadastrarReservaRequest request) {
        return reservaService.realizarReserva(request);
    }

    @GetMapping("/solicitantes/{idSolicitante}")
    @ResponseStatus(HttpStatus.OK)
    public Page<Reserva> listarReservaPorSolicitante(@PathVariable Long idSolicitante,
            @PageableDefault(sort = "periodo_dataHoraFinal", direction = Direction.DESC) @ApiIgnore Pageable page) {
        return reservaService.listarReservaPorSolicitante(idSolicitante, page);
    }

    @GetMapping(value = "/solicitantes/{idSolicitante}", params = { "dataHoraInicial", "dataHoraFinal" })
    @ResponseStatus(HttpStatus.OK)
    public Page<Reserva> listarReservasPorSolicitanteComFiltro(@PathVariable Long idSolicitante,
    @PageableDefault(sort = "periodo_dataHoraFinal", direction = Direction.DESC) @ApiIgnore Pageable page,
    @RequestParam(required = true, name = "dataHoraInicial") String dataHoraInicial,
    @RequestParam(required = true, name = "dataHoraFinal") String dataHoraFinal) {
        return reservaService.listarReservasPorSolicitanteComFiltro(idSolicitante, dataHoraInicial, dataHoraFinal, page);
    }

    @GetMapping(value = "/anuncios/anunciantes/{idAnunciante}")
    @ResponseStatus(HttpStatus.OK)
    public Page<Reserva> listarReservasPorAnunciante(@PathVariable Long idAnunciante,
            @PageableDefault(sort = "periodo_dataHoraFinal", direction = Direction.DESC) @ApiIgnore Pageable page) {
        return reservaService.listarReservaPorAnunciante(idAnunciante, page);
    }

    @PutMapping(value = "{idReserva}/pagamentos")
    @ResponseStatus(HttpStatus.OK)
    public void pagarReserva(@PathVariable Long idReserva, @RequestBody FormaPagamento formaPagamento) {
        reservaService.pagarReserva(idReserva, formaPagamento);
    }

    @PutMapping(value = "{idReserva}/pagamentos/cancelar")
    @ResponseStatus(HttpStatus.OK)
    public void cancelarReserva(@PathVariable Long idReserva) {
        reservaService.cancelarReserva(idReserva);
    }

    @PutMapping(value = "{idReserva}/pagamentos/estornar")
    @ResponseStatus(HttpStatus.OK)
    public void estornarReserva(@PathVariable Long idReserva){
        reservaService.estornarReserva(idReserva);
    }
}
