package com.bclaud.reservas.services.dto.anuncio;

import java.math.BigDecimal;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.bclaud.reservas.domain.FormaPagamento;
import com.bclaud.reservas.domain.TipoAnuncio;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CadastrarAnuncioRequest {
 
    @NotNull
    private Long idImovel;

    @NotNull
    private Long idAnunciante;

    @NotNull
    private TipoAnuncio tipoAnuncio;

    @NotNull
    private BigDecimal valorDiaria;

    @NotNull
    private List<FormaPagamento> formasAceitas;

    @NotBlank
    @Size(min = 0, max = 255)
    private String descricao;
}
