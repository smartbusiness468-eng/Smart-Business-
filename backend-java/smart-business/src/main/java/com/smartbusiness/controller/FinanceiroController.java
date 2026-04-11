package com.smartbusiness.controller;

import com.smartbusiness.dto.ContaPagarDTO;
import com.smartbusiness.dto.ContaReceberDTO;
import com.smartbusiness.entity.ContaPagar;
import com.smartbusiness.entity.ContaReceber;
import com.smartbusiness.service.FinanceiroService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/financeiro")
@RequiredArgsConstructor
@Tag(name = "Financeiro", description = "Gestão de contas a pagar e contas a receber")
public class FinanceiroController {

    private final FinanceiroService financeiroService;

    // ── Contas a Pagar ──────────────────────────────────────────────────────────

    @GetMapping("/contas-pagar")
    @Operation(summary = "Listar contas a pagar")
    public ResponseEntity<List<ContaPagar>> listarContasPagar() {
        return ResponseEntity.ok(financeiroService.listarContasPagar());
    }

    @GetMapping("/contas-pagar/{id}")
    @Operation(summary = "Buscar conta a pagar por ID")
    public ResponseEntity<ContaPagar> buscarContaPagar(@PathVariable Long id) {
        return ResponseEntity.ok(financeiroService.buscarContaPagarPorId(id));
    }

    @PostMapping("/contas-pagar")
    @Operation(summary = "Cadastrar conta a pagar")
    public ResponseEntity<ContaPagar> criarContaPagar(@Valid @RequestBody ContaPagarDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(financeiroService.salvarContaPagar(dto));
    }

    @PutMapping("/contas-pagar/{id}")
    @Operation(summary = "Atualizar conta a pagar")
    public ResponseEntity<ContaPagar> atualizarContaPagar(@PathVariable Long id, @Valid @RequestBody ContaPagarDTO dto) {
        return ResponseEntity.ok(financeiroService.atualizarContaPagar(id, dto));
    }

    @PatchMapping("/contas-pagar/{id}/pagar")
    @Operation(summary = "Marcar conta como paga")
    public ResponseEntity<ContaPagar> marcarComoPaga(@PathVariable Long id) {
        return ResponseEntity.ok(financeiroService.marcarComoPaga(id));
    }

    @DeleteMapping("/contas-pagar/{id}")
    @Operation(summary = "Excluir conta a pagar")
    public ResponseEntity<Void> excluirContaPagar(@PathVariable Long id) {
        financeiroService.excluirContaPagar(id);
        return ResponseEntity.noContent().build();
    }

    // ── Contas a Receber ─────────────────────────────────────────────────────────

    @GetMapping("/contas-receber")
    @Operation(summary = "Listar contas a receber")
    public ResponseEntity<List<ContaReceber>> listarContasReceber() {
        return ResponseEntity.ok(financeiroService.listarContasReceber());
    }

    @GetMapping("/contas-receber/{id}")
    @Operation(summary = "Buscar conta a receber por ID")
    public ResponseEntity<ContaReceber> buscarContaReceber(@PathVariable Long id) {
        return ResponseEntity.ok(financeiroService.buscarContaReceberPorId(id));
    }

    @PostMapping("/contas-receber")
    @Operation(summary = "Cadastrar conta a receber")
    public ResponseEntity<ContaReceber> criarContaReceber(@Valid @RequestBody ContaReceberDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(financeiroService.salvarContaReceber(dto));
    }

    @PutMapping("/contas-receber/{id}")
    @Operation(summary = "Atualizar conta a receber")
    public ResponseEntity<ContaReceber> atualizarContaReceber(@PathVariable Long id, @Valid @RequestBody ContaReceberDTO dto) {
        return ResponseEntity.ok(financeiroService.atualizarContaReceber(id, dto));
    }

    @PatchMapping("/contas-receber/{id}/receber")
    @Operation(summary = "Marcar conta como recebida")
    public ResponseEntity<ContaReceber> marcarComoRecebida(@PathVariable Long id) {
        return ResponseEntity.ok(financeiroService.marcarComoRecebida(id));
    }

    @DeleteMapping("/contas-receber/{id}")
    @Operation(summary = "Excluir conta a receber")
    public ResponseEntity<Void> excluirContaReceber(@PathVariable Long id) {
        financeiroService.excluirContaReceber(id);
        return ResponseEntity.noContent().build();
    }
}
