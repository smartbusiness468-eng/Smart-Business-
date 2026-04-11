package com.smartbusiness.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "pagamentos_pix")
@Getter
@Setter
@NoArgsConstructor
public class PagamentoPix {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @OneToOne(optional = false)
    @JoinColumn(name = "venda_id", nullable = false, unique = true)
    private Venda venda;

    @Column(nullable = false, unique = true, length = 100)
    private String txid;

    @Column(nullable = false, precision = 14, scale = 2)
    private BigDecimal valor;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private StatusPagamento status = StatusPagamento.PENDENTE;

    @Column(length = 1000)
    private String qrCodeUrl;

    @Column(columnDefinition = "TEXT")
    private String pixCopiaECola;

    private LocalDateTime criadoEm;
    private LocalDateTime expiracaoEm;
    private LocalDateTime pagoEm;

    @Column(length = 100)
    private String endToEndId;

    @Column(length = 150)
    private String recebedorNome;

    @Column(length = 150)
    private String recebedorChavePix;

    @Column(columnDefinition = "TEXT")
    private String payloadWebhook;

    @PrePersist
    public void prePersist() {
        this.criadoEm = LocalDateTime.now();
    }
}
