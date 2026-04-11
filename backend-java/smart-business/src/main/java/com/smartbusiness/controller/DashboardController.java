package com.smartbusiness.controller;

import com.smartbusiness.dto.DashboardDTO;
import com.smartbusiness.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@Tag(name = "Dashboard", description = "Indicadores e resumo financeiro da empresa")
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping
    @Operation(summary = "Indicadores do dashboard", description = "Retorna total de vendas do mês, contas a pagar/receber, produtos com estoque baixo e últimas vendas")
    public ResponseEntity<DashboardDTO> indicadores() {
        return ResponseEntity.ok(dashboardService.obterIndicadores());
    }
}
