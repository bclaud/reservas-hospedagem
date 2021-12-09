package com.bclaud.reservas.domain;

import java.time.LocalDateTime;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_solicitante")
    private Usuario solicitante;

    @ManyToOne
    @JoinColumn(name = "id_anuncio")
    private Anuncio anuncio;

    @Embedded
    private Periodo periodo;

    private Integer quantidadePessoas;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dataHoraReserva;

    @Embedded
    private Pagamento pagamento;

    public boolean estaAtiva() {
        if (!this.pagamento.getStatus().equals(StatusPagamento.CANCELADO)
                && !this.pagamento.getStatus().equals(StatusPagamento.ESTORNADO)) {
            return true;
        }
        return false;
    }

    public boolean estaPagamentoPendente() {
        if (this.pagamento.getStatus().equals((StatusPagamento.PENDENTE))) {
            return true;
        }
        return false;
    }

    public boolean estaPago() {
        if (this.pagamento.getStatus().equals((StatusPagamento.PAGO))) {
            return true;
        }
        return false;
    }
}
