package com.bclaud.reservas.exceptions.reserva;

public class QuantidadeDePessoasExceptions extends ReservaExceptions {

    public QuantidadeDePessoasExceptions(String quantidadePessoas, String tipoImovel) {
        super("Não é possivel realizar uma reserva com menos de "+quantidadePessoas+" pessoas para imóveis do tipo "+tipoImovel+"");
    }
    
    
}
