package com.smartbusiness.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class EmpresaDTO {

    private Long id;

    @NotBlank(message = "O nome é obrigatório")
    private String nome;

    private String cnpj;

    private String slug;

    private boolean ativa;
}
