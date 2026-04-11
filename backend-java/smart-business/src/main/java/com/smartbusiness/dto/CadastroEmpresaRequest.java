package com.smartbusiness.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CadastroEmpresaRequest {

    @NotBlank(message = "O nome da empresa é obrigatório")
    private String nomeEmpresa;

    private String cnpj;

    @NotBlank(message = "O nome do administrador é obrigatório")
    private String nomeAdmin;

    @NotBlank(message = "O email é obrigatório")
    @Email(message = "Informe um email válido")
    private String emailAdmin;

    @NotBlank(message = "A senha é obrigatória")
    private String senhaAdmin;
}
