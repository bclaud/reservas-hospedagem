package com.bclaud.reservas.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import java.time.Month;

import com.bclaud.reservas.domain.Periodo;
import com.bclaud.reservas.exceptions.periodo.PeriodoExceptions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class PeriodoServiceTest {

    @InjectMocks
    PeriodoService periodoService;

    Periodo periodo = new Periodo();

    Periodo periodoInvalido_FinalMaiorQueInicio = new Periodo();
    Periodo periodoInvalido_InicioIgualFinal = new Periodo();

    @BeforeEach
    public void init() {
        periodo.setDataHoraInicial(LocalDateTime.of(2022, Month.JANUARY, 1, 14, 0));
        periodo.setDataHoraFinal(LocalDateTime.of(2022, Month.JANUARY, 6, 12, 0));

        periodoInvalido_FinalMaiorQueInicio.setDataHoraInicial(LocalDateTime.of(2022, Month.JANUARY, 6, 12, 0));
        periodoInvalido_FinalMaiorQueInicio.setDataHoraFinal(LocalDateTime.of(2022, Month.JANUARY, 1, 14, 0));

        periodoInvalido_InicioIgualFinal.setDataHoraInicial(LocalDateTime.of(2022, Month.JANUARY, 1, 14, 00));
        periodoInvalido_InicioIgualFinal.setDataHoraFinal(LocalDateTime.of(2022, Month.JANUARY, 1, 12, 00));
    }

    @Test
    void calcularQuantidadeDiarias_RetornaQuantidadeDiarias() {
        Integer expected = 5;

        assertEquals(expected, periodoService.calcularQuantidadeDiarias(periodo));
    }

    @Test
    void calcularQuantidadeDiariasEmHoras_RetornaQuantidadeDeDiariasEmHoras() {
        Long expected = 118L;

        assertEquals(expected, periodoService.calcularQuantidadeDiariasEmHoras(periodo));
    }

    @Test
    void checkPeriodo_RetornaExcecaoPeriodoInvalido() {
        assertThrows(PeriodoExceptions.class, () -> periodoService.checkPeriodo(periodoInvalido_FinalMaiorQueInicio));
    }

    @Test
    void checkPeriodo_MenosQueUmaDiaria() {
        assertThrows(PeriodoExceptions.class, () -> periodoService.checkPeriodo(periodoInvalido_InicioIgualFinal));
    }
}
