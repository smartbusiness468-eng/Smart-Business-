package com.smartbusiness.service;

import com.smartbusiness.dto.DashboardDTO;
import com.smartbusiness.entity.Empresa;
import com.smartbusiness.repository.ClienteRepository;
import com.smartbusiness.repository.ProdutoRepository;
import com.smartbusiness.repository.VendaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final ClienteRepository clienteRepository;
    private final ProdutoRepository produtoRepository;
    private final VendaRepository vendaRepository;
    private final FinanceiroService financeiroService;
    private final AuthService authService;

    public DashboardDTO obterIndicadores() {
        Empresa empresa = authService.usuarioLogado().getEmpresa();

        LocalDate inicioMes = LocalDate.now().withDayOfMonth(1);
        LocalDate fimMes = LocalDate.now();

        BigDecimal vendasMes = vendaRepository.somaTotalVendasPorPeriodo(empresa, inicioMes, fimMes);
        BigDecimal totalPagar = financeiroService.totalPagar();
        BigDecimal totalReceber = financeiroService.totalReceber();

        return DashboardDTO.builder()
                .totalClientes(clienteRepository.countByEmpresa(empresa))
                .totalProdutos(produtoRepository.countByEmpresa(empresa))
                .totalVendas(vendaRepository.countByEmpresa(empresa))
                .vendasMesAtual(vendasMes)
                .totalPagar(totalPagar)
                .totalReceber(totalReceber)
                .saldoPrevisto(totalReceber.subtract(totalPagar))
                .build();
    }
}
