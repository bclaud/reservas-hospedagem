package com.bclaud.reservas.services;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class PagamentoServiceTest {

    @InjectMocks
    PagamentoService pagamentoService;

    @Test
    void calcularValorTotalDiarias_RetornaValorTotalPagamento() {
        Integer quantidadeDias = 10;
        BigDecimal valorDiaria = BigDecimal.valueOf(100);

        BigDecimal expectedValue = BigDecimal.valueOf(1000);

        assertEquals(expectedValue, pagamentoService.calcularValorTotalDiarias(quantidadeDias, valorDiaria));
    }
}
