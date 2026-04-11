package com.smartbusiness.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class PixStatusResponse {
    private String txid;
    private String status;
    private String endToEndId;
    private LocalDateTime pagoEm;
}
