package com.smartbusiness.dto;

import lombok.Data;

@Data
public class PixWebhookRequest {
    private String txid;
    private String endToEndId;
    private String payloadBruto;
}
