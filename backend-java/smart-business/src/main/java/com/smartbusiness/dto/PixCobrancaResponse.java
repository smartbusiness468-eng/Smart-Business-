package com.smartbusiness.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class PixCobrancaResponse {
    private String txid;
    private BigDecimal valor;
    private String qrCodeUrl;
    private String pixCopiaECola;
    private LocalDateTime expiracaoEm;
    private String status;
}
