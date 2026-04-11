package com.smartbusiness.dto;

import com.smartbusiness.entity.FormaPagamento;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class PdvRequest {

    private Long clienteId;

    @NotNull(message = "A forma de pagamento é obrigatória")
    private FormaPagamento formaPagamento;

    private BigDecimal valorRecebido;

    @NotEmpty(message = "Adicione pelo menos um item")
    @Valid
    private List<ItemVendaRequest> itens;
}
