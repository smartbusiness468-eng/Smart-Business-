package com.smartbusiness.service;

import com.smartbusiness.dto.EmpresaDTO;
import com.smartbusiness.entity.Empresa;
import com.smartbusiness.repository.EmpresaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EmpresaService {

    private final EmpresaRepository empresaRepository;
    private final AuthService authService;

    public Empresa buscarEmpresaAtual() {
        return authService.usuarioLogado().getEmpresa();
    }

    @Transactional
    public Empresa atualizar(EmpresaDTO dto) {
        Empresa empresa = buscarEmpresaAtual();
        empresa.setNome(dto.getNome());
        empresa.setCnpj(dto.getCnpj());
        return empresaRepository.save(empresa);
    }
}
