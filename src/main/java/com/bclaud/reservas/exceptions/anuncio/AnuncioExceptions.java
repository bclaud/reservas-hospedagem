package com.bclaud.reservas.exceptions.anuncio;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class AnuncioExceptions extends RuntimeException {
    public AnuncioExceptions(String msg){
        super(msg);
    }
}
