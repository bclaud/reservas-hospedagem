package com.bclaud.reservas.exceptions.reserva;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ReservaExceptions extends RuntimeException {
    public ReservaExceptions(String msg) {
        super(msg);
    }
}
