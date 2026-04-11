package com.smartbusiness.dto;

import com.smartbusiness.entity.FormaPagamento;
import com.smartbusiness.entity.StatusPagamento;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class VendaResponse {

    private Long id;
    private Long clienteId;
    private String clienteNome;
    private LocalDate dataVenda;
    private BigDecimal valorTotal;
    private BigDecimal valorRecebido;
    private BigDecimal troco;
    private FormaPagamento formaPagamento;
    private StatusPagamento statusPagamento;
    private String observacoes;
    private List<ItemVendaResponse> itens;

    @Data
    public static class ItemVendaResponse {
        private Long id;
        private Long produtoId;
        private String produtoNome;
        private Integer quantidade;
        private BigDecimal precoUnitario;
        private BigDecimal subtotal;
    }
}
