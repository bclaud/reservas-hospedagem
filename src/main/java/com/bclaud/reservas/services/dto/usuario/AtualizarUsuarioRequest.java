package com.bclaud.reservas.services.dto.usuario;

import java.time.LocalDate;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.bclaud.reservas.domain.Endereco;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AtualizarUsuarioRequest {

    @NotBlank
    @Size(min = 0, max = 255)
    private String nome;

    @NotBlank
    @Email
    @Size(min = 0, max = 255)
    private String email;

    @NotBlank
    @Size(min = 0, max = 255)
    private String senha;

    @NotNull
    private LocalDate dataNascimento;

    private Endereco endereco;
}
