package com.smartbusiness.pix;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface PixGateway {

    PixCobranca criarCobranca(String txid, BigDecimal valor, int expiracaoSegundos);

    PixStatus consultarStatus(String txid);

    boolean validarTokenWebhook(String token);

    record PixCobranca(
            String txid,
            String qrCodeUrl,
            String copiaECola,
            LocalDateTime expiracaoEm
    ) {}

    record PixStatus(
            boolean pago,
            String endToEndId
    ) {}
}
