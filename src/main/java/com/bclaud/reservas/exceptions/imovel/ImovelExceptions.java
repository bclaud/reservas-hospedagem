package com.bclaud.reservas.exceptions.imovel;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ImovelExceptions extends RuntimeException{
    public ImovelExceptions(String msg){
        super(msg);
    }
}
