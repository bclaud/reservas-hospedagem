package com.bclaud.reservas.repositories;

import com.bclaud.reservas.domain.Anuncio;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface AnuncioRepository extends JpaRepository<Anuncio, Long> {

    Page<Anuncio> findByAnunciante_id(Long id, Pageable page);
    
    @Modifying
    @Transactional
    @Query(value = "UPDATE ANUNCIO SET deleted = true WHERE id=?1", nativeQuery = true)
    public void softDelete(@Param("id") Long id);
}
