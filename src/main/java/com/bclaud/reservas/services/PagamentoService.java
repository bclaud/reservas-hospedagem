package com.bclaud.reservas.services;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

@Service
public class PagamentoService {
    
    public BigDecimal calcularValorTotalDiarias(Integer quantidadeDias, BigDecimal valorDiaria){
        return valorDiaria.multiply(BigDecimal.valueOf(quantidadeDias));
    }
}
