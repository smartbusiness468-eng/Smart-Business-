package com.smartbusiness.controller;

import com.smartbusiness.dto.PixCobrancaResponse;
import com.smartbusiness.dto.PixStatusResponse;
import com.smartbusiness.dto.PixWebhookRequest;
import com.smartbusiness.service.PixService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pix")
@RequiredArgsConstructor
@Tag(name = "PIX", description = "Geração e consulta de cobranças PIX para pagamento de vendas")
public class PixController {

    private final PixService pixService;

    @PostMapping("/vendas/{vendaId}/cobrar")
    @Operation(summary = "Gerar cobrança PIX", description = "Cria uma cobrança PIX para a venda informada e retorna o QR Code")
    public ResponseEntity<PixCobrancaResponse> gerarCobranca(@PathVariable Long vendaId) {
        return ResponseEntity.ok(pixService.gerarCobranca(vendaId));
    }

    @GetMapping("/status/{txid}")
    @Operation(summary = "Consultar status do pagamento", description = "Verifica se o PIX com o txid informado já foi pago")
    public ResponseEntity<PixStatusResponse> consultarStatus(@PathVariable String txid) {
        return ResponseEntity.ok(pixService.consultarStatus(txid));
    }

    @PostMapping("/webhook")
    @Operation(summary = "Webhook de confirmação PIX", description = "Recebe a notificação do banco quando o PIX é confirmado (uso interno)")
    public ResponseEntity<Void> webhook(
            @RequestBody PixWebhookRequest request,
            @RequestHeader(value = "X-Webhook-Token", required = false) String token) {
        pixService.processarWebhook(request, token);
        return ResponseEntity.ok().build();
    }
}
