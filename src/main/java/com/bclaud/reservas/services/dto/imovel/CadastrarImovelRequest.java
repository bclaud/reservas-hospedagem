package com.bclaud.reservas.services.dto.imovel;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.bclaud.reservas.domain.CaracteristicaImovel;
import com.bclaud.reservas.domain.Endereco;
import com.bclaud.reservas.domain.TipoImovel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CadastrarImovelRequest {

    @NotNull
    @Size(min = 0, max = 255)
    private String identificacao;

    @NotNull
    private TipoImovel tipoImovel;

    @Valid
    private Endereco endereco;

    @NotNull
    private Long idProprietario;

    private List<CaracteristicaImovel> caracteristicas;

}
