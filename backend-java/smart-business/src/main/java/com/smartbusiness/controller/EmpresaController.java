package com.smartbusiness.controller;

import com.smartbusiness.dto.EmpresaDTO;
import com.smartbusiness.entity.Empresa;
import com.smartbusiness.service.EmpresaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/empresa")
@RequiredArgsConstructor
@Tag(name = "Empresa", description = "Dados e configurações da empresa logada (somente ADMIN)")
public class EmpresaController {

    private final EmpresaService empresaService;

    @GetMapping
    @Operation(summary = "Buscar dados da empresa", description = "Retorna nome, CNPJ e demais dados da empresa do usuário logado")
    public ResponseEntity<Empresa> buscar() {
        return ResponseEntity.ok(empresaService.buscarEmpresaAtual());
    }

    @PutMapping
    @Operation(summary = "Atualizar dados da empresa")
    public ResponseEntity<Empresa> atualizar(@Valid @RequestBody EmpresaDTO dto) {
        return ResponseEntity.ok(empresaService.atualizar(dto));
    }
}
