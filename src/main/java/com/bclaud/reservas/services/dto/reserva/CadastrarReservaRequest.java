package com.bclaud.reservas.services.dto.reserva;

import javax.validation.constraints.NotNull;

import com.bclaud.reservas.domain.Periodo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CadastrarReservaRequest {
    
    @NotNull
    private Long idSolicitante;
    @NotNull
    private Long idAnuncio;
    @NotNull
    private Periodo periodo;
    @NotNull
    private Integer quantidadePessoas;

}
