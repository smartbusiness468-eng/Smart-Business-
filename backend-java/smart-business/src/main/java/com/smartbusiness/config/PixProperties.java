package com.smartbusiness.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "pix")
@Getter
@Setter
public class PixProperties {
    private String recebedorNome;
    private String chave;
    private int expiracaoSegundos = 900;
    private String webhookToken;
}
