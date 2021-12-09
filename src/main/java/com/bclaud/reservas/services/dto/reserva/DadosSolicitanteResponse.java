package com.bclaud.reservas.services.dto.reserva;

import com.bclaud.reservas.domain.Usuario;

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
public class DadosSolicitanteResponse {
    
    private long id;

    private String nome;

    public static DadosSolicitanteResponse of(Usuario usuario){
        return DadosSolicitanteResponse.builder()
        .id(usuario.getId())
        .nome(usuario.getNome())
        .build();
    }
}
