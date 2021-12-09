package com.bclaud.reservas.repositories;

import com.bclaud.reservas.domain.CaracteristicaImovel;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CaracteristicaImovelRepository extends JpaRepository<CaracteristicaImovel, Long> {

}
