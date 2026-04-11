package com.smartbusiness.service;

import com.smartbusiness.dto.ClienteDTO;
import com.smartbusiness.entity.Cliente;
import com.smartbusiness.entity.Empresa;
import com.smartbusiness.exception.ResourceNotFoundException;
import com.smartbusiness.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final AuthService authService;

    public List<Cliente> listarTodos() {
        Empresa empresa = authService.usuarioLogado().getEmpresa();
        return clienteRepository.findByEmpresa(empresa);
    }

    public Cliente buscarPorId(Long id) {
        Empresa empresa = authService.usuarioLogado().getEmpresa();
        return clienteRepository.findByIdAndEmpresa(id, empresa)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));
    }

    @Transactional
    public Cliente salvar(ClienteDTO dto) {
        Empresa empresa = authService.usuarioLogado().getEmpresa();

        Cliente cliente = new Cliente();
        cliente.setNome(dto.getNome());
        cliente.setEmail(dto.getEmail());
        cliente.setTelefone(dto.getTelefone());
        cliente.setCpfCnpj(dto.getCpfCnpj());
        cliente.setEmpresa(empresa);

        return clienteRepository.save(cliente);
    }

    @Transactional
    public Cliente atualizar(Long id, ClienteDTO dto) {
        Cliente cliente = buscarPorId(id);
        cliente.setNome(dto.getNome());
        cliente.setEmail(dto.getEmail());
        cliente.setTelefone(dto.getTelefone());
        cliente.setCpfCnpj(dto.getCpfCnpj());
        return clienteRepository.save(cliente);
    }

    @Transactional
    public void excluir(Long id) {
        Cliente cliente = buscarPorId(id);
        clienteRepository.delete(cliente);
    }
}
