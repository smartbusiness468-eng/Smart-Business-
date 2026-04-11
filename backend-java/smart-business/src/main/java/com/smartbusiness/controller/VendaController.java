package com.smartbusiness.controller;

import com.smartbusiness.dto.VendaRequest;
import com.smartbusiness.dto.VendaResponse;
import com.smartbusiness.service.VendaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vendas")
@RequiredArgsConstructor
@Tag(name = "Vendas", description = "Registro e gestão de vendas com baixa automática de estoque")
public class VendaController {

    private final VendaService vendaService;

    @GetMapping
    @Operation(summary = "Listar vendas", description = "Retorna todas as vendas da empresa logada")
    public ResponseEntity<List<VendaResponse>> listar() {
        return ResponseEntity.ok(vendaService.listarTodas());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar venda por ID")
    public ResponseEntity<VendaResponse> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(vendaService.buscarPorId(id));
    }

    @PostMapping
    @Operation(summary = "Registrar venda", description = "Cria uma nova venda e deduz automaticamente o estoque dos produtos")
    public ResponseEntity<VendaResponse> criar(@Valid @RequestBody VendaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(vendaService.salvar(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Editar venda", description = "Atualiza uma venda existente e reajusta o estoque")
    public ResponseEntity<VendaResponse> atualizar(@PathVariable Long id, @Valid @RequestBody VendaRequest request) {
        return ResponseEntity.ok(vendaService.atualizar(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir venda", description = "Remove a venda e devolve os itens ao estoque")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        vendaService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
