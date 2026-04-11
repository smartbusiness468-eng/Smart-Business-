package com.smartbusiness.service;

import com.smartbusiness.dto.UsuarioDTO;
import com.smartbusiness.entity.Empresa;
import com.smartbusiness.entity.Usuario;
import com.smartbusiness.exception.BusinessException;
import com.smartbusiness.exception.ResourceNotFoundException;
import com.smartbusiness.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthService authService;

    public List<Usuario> listarTodos() {
        Empresa empresa = authService.usuarioLogado().getEmpresa();
        return usuarioRepository.findByEmpresa(empresa);
    }

    public Usuario buscarPorId(Long id) {
        Empresa empresa = authService.usuarioLogado().getEmpresa();
        return usuarioRepository.findById(id)
                .filter(u -> u.getEmpresa() != null && u.getEmpresa().getId().equals(empresa.getId()))
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));
    }

    @Transactional
    public Usuario salvar(UsuarioDTO dto) {
        Empresa empresa = authService.usuarioLogado().getEmpresa();

        if (usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new BusinessException("Este email já está em uso");
        }

        Usuario usuario = new Usuario();
        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        usuario.setSenha(passwordEncoder.encode(dto.getSenha()));
        usuario.setPerfil(dto.getPerfil());
        usuario.setAtivo(true);
        usuario.setEmpresa(empresa);

        return usuarioRepository.save(usuario);
    }

    @Transactional
    public Usuario atualizar(Long id, UsuarioDTO dto) {
        Usuario usuario = buscarPorId(id);
        usuario.setNome(dto.getNome());
        usuario.setPerfil(dto.getPerfil());
        usuario.setAtivo(dto.isAtivo());

        if (dto.getSenha() != null && !dto.getSenha().isBlank()) {
            usuario.setSenha(passwordEncoder.encode(dto.getSenha()));
        }

        return usuarioRepository.save(usuario);
    }

    @Transactional
    public void excluir(Long id) {
        Usuario usuarioLogado = authService.usuarioLogado();
        Usuario usuario = buscarPorId(id);

        if (usuario.getId().equals(usuarioLogado.getId())) {
            throw new BusinessException("Não é possível excluir o próprio usuário");
        }

        usuarioRepository.delete(usuario);
    }
}
