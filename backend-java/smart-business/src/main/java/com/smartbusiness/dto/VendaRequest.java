package com.smartbusiness.dto;

import com.smartbusiness.entity.FormaPagamento;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class VendaRequest {

    private Long clienteId;

    private LocalDate dataVenda;

    private String observacoes;

    @NotNull(message = "A forma de pagamento é obrigatória")
    private FormaPagamento formaPagamento;

    private BigDecimal valorRecebido;

    @NotEmpty(message = "A venda deve ter pelo menos um item")
    @Valid
    private List<ItemVendaRequest> itens;
}
