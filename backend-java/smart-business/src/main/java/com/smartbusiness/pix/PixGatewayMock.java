package com.smartbusiness.pix;

import com.smartbusiness.config.PixProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class PixGatewayMock implements PixGateway {

    private final PixProperties pixProperties;

    @Override
    public PixCobranca criarCobranca(String txid, BigDecimal valor, int expiracaoSegundos) {
        String qrCodeUrl = "https://geradormockpix.com/qr/" + txid;
        String copiaECola = "00020126360014BR.GOV.BCB.PIX0114" + pixProperties.getChave()
                + "5204000053039865802BR5913"
                + pixProperties.getRecebedorNome()
                + "6009SAO PAULO62070503***6304MOCK";

        return new PixCobranca(txid, qrCodeUrl, copiaECola,
                LocalDateTime.now().plusSeconds(expiracaoSegundos));
    }

    @Override
    public PixStatus consultarStatus(String txid) {
        return new PixStatus(false, null);
    }

    @Override
    public boolean validarTokenWebhook(String token) {
        return pixProperties.getWebhookToken().equals(token);
    }
}
