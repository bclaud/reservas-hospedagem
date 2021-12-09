package com.bclaud.reservas.repositories;

import java.time.LocalDateTime;
import java.util.List;

import com.bclaud.reservas.domain.Reserva;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long>, JpaSpecificationExecutor<Reserva> {

    Page<Reserva> findBySolicitante_id(Long idSolicitante, Pageable page);
    Page<Reserva> findByAnuncio_Anunciante_Id(Long idAnunciante, Pageable page);

    List<Reserva> findByAnuncioIdAndPeriodoDataHoraInicialLessThanEqualAndPeriodoDataHoraFinalGreaterThanEqual(Long anuncioId, LocalDateTime dataHoraFinal, LocalDateTime dataHoraInicial);

    Page<Reserva> findBySolicitanteIdAndPeriodoDataHoraInicialGreaterThanEqualAndPeriodoDataHoraFinalLessThanEqual(Long solicitanteId, LocalDateTime dataHoraInicial, LocalDateTime dataHoraFinal, Pageable page);
}


