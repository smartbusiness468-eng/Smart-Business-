package com.smartbusiness.controller;

import com.smartbusiness.dto.PdvRequest;
import com.smartbusiness.dto.ProdutoDTO;
import com.smartbusiness.dto.VendaRequest;
import com.smartbusiness.dto.VendaResponse;
import com.smartbusiness.entity.Produto;
import com.smartbusiness.service.ProdutoService;
import com.smartbusiness.service.VendaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/pdv")
@RequiredArgsConstructor
@Tag(name = "PDV", description = "Ponto de Venda — consulta de produtos disponíveis e finalização de vendas no caixa")
public class PdvController {

    private final VendaService vendaService;
    private final ProdutoService produtoService;

    @GetMapping("/produtos")
    @Operation(summary = "Produtos disponíveis no caixa", description = "Retorna apenas produtos com estoque maior que zero")
    public ResponseEntity<List<Produto>> produtosDisponiveis() {
        return ResponseEntity.ok(
                produtoService.listarTodos().stream()
                        .filter(p -> p.getQuantidadeEstoque() != null && p.getQuantidadeEstoque() > 0)
                        .toList()
        );
    }

    @PostMapping("/finalizar")
    @Operation(summary = "Finalizar venda no caixa", description = "Registra a venda, deduz o estoque e calcula troco se o pagamento for em dinheiro")
    public ResponseEntity<VendaResponse> finalizar(@Valid @RequestBody PdvRequest request) {
        VendaRequest vendaRequest = new VendaRequest();
        vendaRequest.setClienteId(request.getClienteId());
        vendaRequest.setFormaPagamento(request.getFormaPagamento());
        vendaRequest.setValorRecebido(request.getValorRecebido());
        vendaRequest.setItens(request.getItens());
        vendaRequest.setDataVenda(LocalDate.now());

        return ResponseEntity.status(HttpStatus.CREATED).body(vendaService.salvar(vendaRequest));
    }
}
