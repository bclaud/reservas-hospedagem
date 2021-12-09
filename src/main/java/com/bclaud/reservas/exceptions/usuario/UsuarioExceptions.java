package com.bclaud.reservas.exceptions.usuario;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UsuarioExceptions extends RuntimeException {

    public UsuarioExceptions(String msg) {
        super(msg);
    }
}
