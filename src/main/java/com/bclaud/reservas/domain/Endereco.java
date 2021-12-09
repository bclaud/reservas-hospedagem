package com.bclaud.reservas.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Endereco {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = Access.READ_ONLY)
    private Long id;

    @Pattern(regexp = "[0-9]{5}-[0-9]{3}", message = "O CEP deve ser informado no formato 99999-999.")
    private String cep;

    @NotBlank
    @Size(min = 0, max = 255)
    private String logradouro;

    @NotBlank
    @Size(min = 0, max = 255)
    private String numero;

    @Size(min = 0, max = 255)
    private String complemento;

    @NotBlank
    @Size(min = 0, max = 255)
    private String bairro;

    @NotBlank
    @Size(min = 0, max = 255)
    private String cidade;

    @NotBlank
    @Size(min = 0, max = 255)
    private String estado;
}
