package com.smartbusiness.service;

import com.smartbusiness.dto.ProdutoDTO;
import com.smartbusiness.entity.Empresa;
import com.smartbusiness.entity.Perfil;
import com.smartbusiness.entity.Produto;
import com.smartbusiness.entity.Usuario;
import com.smartbusiness.exception.ResourceNotFoundException;
import com.smartbusiness.repository.ProdutoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProdutoServiceTest {

    @Mock private ProdutoRepository produtoRepository;
    @Mock private AuthService authService;

    @InjectMocks
    private ProdutoService produtoService;

    private Empresa empresa;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        empresa = new Empresa();
        empresa.setNome("Loja");

        usuario = new Usuario();
        usuario.setEmail("admin@loja.com");
        usuario.setPerfil(Perfil.ADMIN);
        usuario.setEmpresa(empresa);
    }

    @Test
    void salvar_deveCriarProdutoComDadosCorretos() {
        ProdutoDTO dto = new ProdutoDTO();
        dto.setNome("Caderno");
        dto.setPreco(new BigDecimal("12.90"));
        dto.setQuantidadeEstoque(50);

        when(authService.usuarioLogado()).thenReturn(usuario);
        when(produtoRepository.save(any(Produto.class))).thenAnswer(inv -> inv.getArgument(0));

        Produto salvo = produtoService.salvar(dto);

        assertThat(salvo.getNome()).isEqualTo("Caderno");
        assertThat(salvo.getPreco()).isEqualByComparingTo("12.90");
        assertThat(salvo.getQuantidadeEstoque()).isEqualTo(50);
        assertThat(salvo.getEmpresa()).isEqualTo(empresa);
    }

    @Test
    void buscarPorId_quandoNaoExiste_deveLancarException() {
        when(authService.usuarioLogado()).thenReturn(usuario);
        when(produtoRepository.findByIdAndEmpresa(1L, empresa)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> produtoService.buscarPorId(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Produto não encontrado");
    }

    @Test
    void atualizar_deveAtualizarCamposCorretamente() {
        Produto existente = new Produto();
        existente.setNome("Borracha");
        existente.setPreco(new BigDecimal("1.00"));
        existente.setQuantidadeEstoque(20);
        existente.setEmpresa(empresa);

        ProdutoDTO dto = new ProdutoDTO();
        dto.setNome("Borracha Premium");
        dto.setPreco(new BigDecimal("2.50"));
        dto.setQuantidadeEstoque(30);

        when(authService.usuarioLogado()).thenReturn(usuario);
        when(produtoRepository.findByIdAndEmpresa(1L, empresa)).thenReturn(Optional.of(existente));
        when(produtoRepository.save(any(Produto.class))).thenAnswer(inv -> inv.getArgument(0));

        Produto atualizado = produtoService.atualizar(1L, dto);

        assertThat(atualizado.getNome()).isEqualTo("Borracha Premium");
        assertThat(atualizado.getPreco()).isEqualByComparingTo("2.50");
        assertThat(atualizado.getQuantidadeEstoque()).isEqualTo(30);
    }
}
