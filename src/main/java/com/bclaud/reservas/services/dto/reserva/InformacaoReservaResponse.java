package com.bclaud.reservas.services.dto.reserva;

import com.bclaud.reservas.domain.Pagamento;
import com.bclaud.reservas.domain.Periodo;
import com.bclaud.reservas.domain.Reserva;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InformacaoReservaResponse {
    
    private Long id;
    private DadosSolicitanteResponse solicitante;
    private DadosAnuncioResponse anuncio;
    private Integer quantidadePessoas;
    private Periodo periodo;
    private Pagamento pagamento;

    public static InformacaoReservaResponse of(Reserva reserva){
        return InformacaoReservaResponse.builder()
        .id(reserva.getId())
        .solicitante(DadosSolicitanteResponse.of(reserva.getSolicitante()))
        .anuncio(DadosAnuncioResponse.of(reserva.getAnuncio()))
        .quantidadePessoas(reserva.getQuantidadePessoas())
        .periodo(reserva.getPeriodo())
        .pagamento(reserva.getPagamento())
        .build();
    }
}
