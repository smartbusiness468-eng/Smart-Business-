package com.smartbusiness.service;

import com.smartbusiness.config.PixProperties;
import com.smartbusiness.dto.PixCobrancaResponse;
import com.smartbusiness.dto.PixStatusResponse;
import com.smartbusiness.dto.PixWebhookRequest;
import com.smartbusiness.entity.PagamentoPix;
import com.smartbusiness.entity.StatusPagamento;
import com.smartbusiness.entity.Venda;
import com.smartbusiness.exception.BusinessException;
import com.smartbusiness.exception.ResourceNotFoundException;
import com.smartbusiness.pix.PixGateway;
import com.smartbusiness.repository.PagamentoPixRepository;
import com.smartbusiness.repository.VendaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PixService {

    private final PagamentoPixRepository pagamentoPixRepository;
    private final VendaRepository vendaRepository;
    private final PixGateway pixGateway;
    private final PixProperties pixProperties;
    private final AuthService authService;

    @Transactional
    public PixCobrancaResponse gerarCobranca(Long vendaId) {
        Venda venda = buscarVendaDaEmpresa(vendaId);

        if (venda.getStatusPagamento() == StatusPagamento.PAGO) {
            throw new BusinessException("Esta venda já está paga.");
        }

        Optional<PagamentoPix> existente = pagamentoPixRepository.findByVendaId(vendaId);
        if (existente.isPresent() && existente.get().getStatus() == StatusPagamento.PAGO) {
            return toResponse(existente.get());
        }

        String txid = gerarTxid();
        PixGateway.PixCobranca cobranca = pixGateway.criarCobranca(
                txid, venda.getValorTotal(), pixProperties.getExpiracaoSegundos());

        PagamentoPix pagamento = existente.orElseGet(PagamentoPix::new);
        pagamento.setVenda(venda);
        pagamento.setTxid(cobranca.txid());
        pagamento.setValor(venda.getValorTotal());
        pagamento.setStatus(StatusPagamento.AGUARDANDO_PIX);
        pagamento.setQrCodeUrl(cobranca.qrCodeUrl());
        pagamento.setPixCopiaECola(cobranca.copiaECola());
        pagamento.setExpiracaoEm(cobranca.expiracaoEm());
        pagamento.setRecebedorNome(pixProperties.getRecebedorNome());
        pagamento.setRecebedorChavePix(pixProperties.getChave());
        pagamentoPixRepository.save(pagamento);

        venda.setStatusPagamento(StatusPagamento.AGUARDANDO_PIX);
        vendaRepository.save(venda);

        return toResponse(pagamento);
    }

    @Transactional
    public PixStatusResponse consultarStatus(String txid) {
        PagamentoPix pagamento = pagamentoPixRepository.findByTxid(txid)
                .orElseThrow(() -> new ResourceNotFoundException("Cobrança PIX não encontrada."));

        if (pagamento.getStatus() == StatusPagamento.PAGO) {
            return new PixStatusResponse(pagamento.getTxid(), pagamento.getStatus().name(),
                    pagamento.getEndToEndId(), pagamento.getPagoEm());
        }

        if (pagamento.getExpiracaoEm() != null && LocalDateTime.now().isAfter(pagamento.getExpiracaoEm())) {
            pagamento.setStatus(StatusPagamento.EXPIRADO);
            pagamentoPixRepository.save(pagamento);
        } else {
            PixGateway.PixStatus statusGateway = pixGateway.consultarStatus(txid);
            if (statusGateway.pago()) {
                confirmarPagamento(pagamento, statusGateway.endToEndId(), "consulta ativa");
            }
        }

        return new PixStatusResponse(pagamento.getTxid(), pagamento.getStatus().name(),
                pagamento.getEndToEndId(), pagamento.getPagoEm());
    }

    @Transactional
    public void processarWebhook(PixWebhookRequest request, String token) {
        if (!pixGateway.validarTokenWebhook(token)) {
            throw new BusinessException("Token de webhook inválido.");
        }

        PagamentoPix pagamento = pagamentoPixRepository.findByTxid(request.getTxid())
                .orElseThrow(() -> new ResourceNotFoundException("Cobrança PIX não encontrada."));

        confirmarPagamento(pagamento, request.getEndToEndId(), request.getPayloadBruto());
    }

    private void confirmarPagamento(PagamentoPix pagamento, String endToEndId, String payload) {
        if (pagamento.getStatus() == StatusPagamento.PAGO) return;

        pagamento.setStatus(StatusPagamento.PAGO);
        pagamento.setPagoEm(LocalDateTime.now());
        pagamento.setEndToEndId(endToEndId);
        pagamento.setPayloadWebhook(payload);
        pagamentoPixRepository.save(pagamento);

        Venda venda = pagamento.getVenda();
        venda.setStatusPagamento(StatusPagamento.PAGO);
        venda.setDataPagamento(LocalDateTime.now());
        vendaRepository.save(venda);
    }

    private Venda buscarVendaDaEmpresa(Long vendaId) {
        var empresa = authService.usuarioLogado().getEmpresa();
        return vendaRepository.findByIdAndEmpresa(vendaId, empresa)
                .orElseThrow(() -> new ResourceNotFoundException("Venda não encontrada."));
    }

    private PixCobrancaResponse toResponse(PagamentoPix p) {
        return new PixCobrancaResponse(p.getTxid(), p.getValor(),
                p.getQrCodeUrl(), p.getPixCopiaECola(), p.getExpiracaoEm(), p.getStatus().name());
    }

    private String gerarTxid() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 24).toUpperCase();
    }
}
