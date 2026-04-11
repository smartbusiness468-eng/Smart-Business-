package com.smartbusiness.dto;

import com.smartbusiness.entity.Perfil;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UsuarioDTO {

    private Long id;

    @NotBlank(message = "O nome é obrigatório")
    private String nome;

    @NotBlank(message = "O email é obrigatório")
    @Email(message = "Informe um email válido")
    private String email;

    private String senha;

    @NotNull(message = "O perfil é obrigatório")
    private Perfil perfil;

    private boolean ativo;
}
