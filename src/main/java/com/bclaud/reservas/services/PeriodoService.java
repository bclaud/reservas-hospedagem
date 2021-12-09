package com.bclaud.reservas.services;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import com.bclaud.reservas.domain.Periodo;
import com.bclaud.reservas.exceptions.periodo.PeriodoExceptions;

import org.springframework.stereotype.Service;

@Service
public class PeriodoService {

    public Integer calcularQuantidadeDiarias(Periodo periodo){
        LocalDate dataInicio = LocalDate.from(periodo.getDataHoraInicial());
        LocalDate dataFim = LocalDate.from(periodo.getDataHoraFinal());

        return dataInicio.until(dataFim).getDays();
    }
    public Long calcularQuantidadeDiariasEmHoras(Periodo periodo){

        return periodo.getDataHoraInicial().until(periodo.getDataHoraFinal(), ChronoUnit.HOURS);        
    }

    public void checkPeriodo(Periodo periodo) throws PeriodoExceptions{
        if(!inicioValido(periodo)){
            throw new PeriodoExceptions("Período inválido! A data final da reserva precisa ser maior do que a data inicial.");
        }

        if(!maiorOuIgualUmaDiaria(periodo)){
            throw new PeriodoExceptions("Período inválido! O número mínimo de diárias precisa ser maior ou igual à 1.");
        }
    }

    private boolean inicioValido(Periodo periodo){
        return periodo.getDataHoraInicial().isBefore(periodo.getDataHoraFinal());
    }

    private boolean maiorOuIgualUmaDiaria(Periodo periodo){
        return calcularQuantidadeDiarias(periodo) >= 1;
    }
}
