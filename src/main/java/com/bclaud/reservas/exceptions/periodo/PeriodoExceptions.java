package com.bclaud.reservas.exceptions.periodo;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PeriodoExceptions extends RuntimeException {

    public PeriodoExceptions(String msg){
        super(msg);
    }
}
