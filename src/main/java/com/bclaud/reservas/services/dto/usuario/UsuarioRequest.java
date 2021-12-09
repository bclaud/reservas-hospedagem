package com.bclaud.reservas.services.dto.usuario;

import java.time.LocalDate;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
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
public class UsuarioRequest {

    @NotBlank
    @Size(min = 0, max = 255)
    private String nome;

    @Email
    @NotBlank
    @Size(min = 0, max = 255)
    private String email;

    @NotBlank
    @Size(min = 0, max = 255)
    private String senha;

    @Pattern(regexp = "[0-9]{11}", message = "O CPF deve ser informado no formato 99999999999.")
    private String cpf;

    @NotNull
    private LocalDate dataNascimento;

    @Valid
    private Endereco endereco;
}
