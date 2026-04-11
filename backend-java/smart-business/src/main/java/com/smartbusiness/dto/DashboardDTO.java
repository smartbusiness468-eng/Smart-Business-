package com.smartbusiness.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class DashboardDTO {
    private long totalClientes;
    private long totalProdutos;
    private long totalVendas;
    private BigDecimal totalReceber;
    private BigDecimal totalPagar;
    private BigDecimal saldoPrevisto;
    private BigDecimal vendasMesAtual;
}
