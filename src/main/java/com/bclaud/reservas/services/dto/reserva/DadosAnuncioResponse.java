package com.bclaud.reservas.services.dto.reserva;

import java.util.List;

import com.bclaud.reservas.domain.Anuncio;
import com.bclaud.reservas.domain.FormaPagamento;
import com.bclaud.reservas.domain.Imovel;

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
public class DadosAnuncioResponse {
    
    private Long id;
    private Imovel imovel;
    private List<FormaPagamento> formasAceitas;
    private String descricao;

    public static DadosAnuncioResponse of(Anuncio anuncio){
        return DadosAnuncioResponse.builder()
        .id(anuncio.getId())
        .imovel(anuncio.getImovel())
        .formasAceitas(anuncio.getFormasAceitas())
        .descricao(anuncio.getDescricao())
        .build();

    }
}
