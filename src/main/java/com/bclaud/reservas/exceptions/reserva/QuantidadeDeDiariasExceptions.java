package com.bclaud.reservas.exceptions.reserva;

public class QuantidadeDeDiariasExceptions extends ReservaExceptions {

    public QuantidadeDeDiariasExceptions(String quantidadeDiarias, String tipoImovel) {
        super("Não é possivel realizar uma reserva com menos de "+quantidadeDiarias+" diárias para imóveis do tipo "+tipoImovel+"");
    }
    
    
}
