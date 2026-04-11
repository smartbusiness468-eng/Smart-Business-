package com.smartbusiness.service;

import com.smartbusiness.dto.ContaPagarDTO;
import com.smartbusiness.dto.ContaReceberDTO;
import com.smartbusiness.entity.ContaPagar;
import com.smartbusiness.entity.ContaReceber;
import com.smartbusiness.entity.Empresa;
import com.smartbusiness.exception.ResourceNotFoundException;
import com.smartbusiness.repository.ContaPagarRepository;
import com.smartbusiness.repository.ContaReceberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FinanceiroService {

    private final ContaPagarRepository contaPagarRepository;
    private final ContaReceberRepository contaReceberRepository;
    private final AuthService authService;

    // ── Contas a Pagar ──────────────────────────────────────────────────────────

    public List<ContaPagar> listarContasPagar() {
        Empresa empresa = authService.usuarioLogado().getEmpresa();
        return contaPagarRepository.findByEmpresaOrderByDataVencimentoAsc(empresa);
    }

    public ContaPagar buscarContaPagarPorId(Long id) {
        Empresa empresa = authService.usuarioLogado().getEmpresa();
        return contaPagarRepository.findByIdAndEmpresa(id, empresa)
                .orElseThrow(() -> new ResourceNotFoundException("Conta a pagar não encontrada"));
    }

    @Transactional
    public ContaPagar salvarContaPagar(ContaPagarDTO dto) {
        Empresa empresa = authService.usuarioLogado().getEmpresa();

        ContaPagar conta = new ContaPagar();
        conta.setDescricao(dto.getDescricao());
        conta.setValor(dto.getValor());
        conta.setDataVencimento(dto.getDataVencimento());
        conta.setPaga(dto.isPaga());
        conta.setDataPagamento(dto.getDataPagamento());
        conta.setEmpresa(empresa);

        return contaPagarRepository.save(conta);
    }

    @Transactional
    public ContaPagar atualizarContaPagar(Long id, ContaPagarDTO dto) {
        ContaPagar conta = buscarContaPagarPorId(id);
        conta.setDescricao(dto.getDescricao());
        conta.setValor(dto.getValor());
        conta.setDataVencimento(dto.getDataVencimento());
        conta.setPaga(dto.isPaga());
        conta.setDataPagamento(dto.getDataPagamento());
        return contaPagarRepository.save(conta);
    }

    @Transactional
    public ContaPagar marcarComoPaga(Long id) {
        ContaPagar conta = buscarContaPagarPorId(id);
        conta.setPaga(true);
        conta.setDataPagamento(LocalDate.now());
        return contaPagarRepository.save(conta);
    }

    @Transactional
    public void excluirContaPagar(Long id) {
        ContaPagar conta = buscarContaPagarPorId(id);
        contaPagarRepository.delete(conta);
    }

    // ── Contas a Receber ─────────────────────────────────────────────────────────

    public List<ContaReceber> listarContasReceber() {
        Empresa empresa = authService.usuarioLogado().getEmpresa();
        return contaReceberRepository.findByEmpresaOrderByDataVencimentoAsc(empresa);
    }

    public ContaReceber buscarContaReceberPorId(Long id) {
        Empresa empresa = authService.usuarioLogado().getEmpresa();
        return contaReceberRepository.findByIdAndEmpresa(id, empresa)
                .orElseThrow(() -> new ResourceNotFoundException("Conta a receber não encontrada"));
    }

    @Transactional
    public ContaReceber salvarContaReceber(ContaReceberDTO dto) {
        Empresa empresa = authService.usuarioLogado().getEmpresa();

        ContaReceber conta = new ContaReceber();
        conta.setDescricao(dto.getDescricao());
        conta.setValor(dto.getValor());
        conta.setDataVencimento(dto.getDataVencimento());
        conta.setRecebida(dto.isRecebida());
        conta.setDataRecebimento(dto.getDataRecebimento());
        conta.setEmpresa(empresa);

        return contaReceberRepository.save(conta);
    }

    @Transactional
    public ContaReceber atualizarContaReceber(Long id, ContaReceberDTO dto) {
        ContaReceber conta = buscarContaReceberPorId(id);
        conta.setDescricao(dto.getDescricao());
        conta.setValor(dto.getValor());
        conta.setDataVencimento(dto.getDataVencimento());
        conta.setRecebida(dto.isRecebida());
        conta.setDataRecebimento(dto.getDataRecebimento());
        return contaReceberRepository.save(conta);
    }

    @Transactional
    public ContaReceber marcarComoRecebida(Long id) {
        ContaReceber conta = buscarContaReceberPorId(id);
        conta.setRecebida(true);
        conta.setDataRecebimento(LocalDate.now());
        return contaReceberRepository.save(conta);
    }

    @Transactional
    public void excluirContaReceber(Long id) {
        ContaReceber conta = buscarContaReceberPorId(id);
        contaReceberRepository.delete(conta);
    }

    // ── Totalizadores ────────────────────────────────────────────────────────────

    public BigDecimal totalPagar() {
        return listarContasPagar().stream()
                .filter(c -> !c.isPaga())
                .map(ContaPagar::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal totalReceber() {
        return listarContasReceber().stream()
                .filter(c -> !c.isRecebida())
                .map(ContaReceber::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
