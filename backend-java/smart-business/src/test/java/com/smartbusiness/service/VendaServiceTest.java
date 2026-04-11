package com.smartbusiness.service;

import com.smartbusiness.dto.ItemVendaRequest;
import com.smartbusiness.dto.VendaRequest;
import com.smartbusiness.dto.VendaResponse;
import com.smartbusiness.entity.*;
import com.smartbusiness.exception.BusinessException;
import com.smartbusiness.exception.ResourceNotFoundException;
import com.smartbusiness.repository.ClienteRepository;
import com.smartbusiness.repository.ProdutoRepository;
import com.smartbusiness.repository.VendaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VendaServiceTest {

    @Mock private VendaRepository vendaRepository;
    @Mock private ClienteRepository clienteRepository;
    @Mock private ProdutoRepository produtoRepository;
    @Mock private AuthService authService;

    @InjectMocks
    private VendaService vendaService;

    private Empresa empresa;
    private Usuario usuario;
    private Produto produto;

    @BeforeEach
    void setUp() {
        empresa = new Empresa();
        empresa.setNome("Loja Teste");

        usuario = new Usuario();
        usuario.setNome("Operador");
        usuario.setPerfil(Perfil.OPERADOR);
        usuario.setEmpresa(empresa);

        produto = new Produto();
        produto.setNome("Caneta");
        produto.setPreco(new BigDecimal("2.50"));
        produto.setQuantidadeEstoque(10);
        produto.setEmpresa(empresa);
    }

    @Test
    void salvar_vendaDinheiro_deveCalcularTrocoCorretamente() {
        ItemVendaRequest item = new ItemVendaRequest();
        item.setProdutoId(1L);
        item.setQuantidade(2);

        VendaRequest request = new VendaRequest();
        request.setFormaPagamento(FormaPagamento.DINHEIRO);
        request.setValorRecebido(new BigDecimal("10.00"));
        request.setItens(List.of(item));

        when(authService.usuarioLogado()).thenReturn(usuario);
        when(produtoRepository.findByIdAndEmpresa(1L, empresa)).thenReturn(Optional.of(produto));
        when(vendaRepository.save(any(Venda.class))).thenAnswer(inv -> inv.getArgument(0));

        VendaResponse response = vendaService.salvar(request);

        assertThat(response.getValorTotal()).isEqualByComparingTo("5.00");
        assertThat(response.getTroco()).isEqualByComparingTo("5.00");
        assertThat(response.getStatusPagamento()).isEqualTo(StatusPagamento.PAGO);
        // Estoque deve ter sido baixado
        assertThat(produto.getQuantidadeEstoque()).isEqualTo(8);
    }

    @Test
    void salvar_vendaPix_deveDefinirStatusAguardando() {
        ItemVendaRequest item = new ItemVendaRequest();
        item.setProdutoId(1L);
        item.setQuantidade(1);

        VendaRequest request = new VendaRequest();
        request.setFormaPagamento(FormaPagamento.PIX);
        request.setItens(List.of(item));

        when(authService.usuarioLogado()).thenReturn(usuario);
        when(produtoRepository.findByIdAndEmpresa(1L, empresa)).thenReturn(Optional.of(produto));
        when(vendaRepository.save(any(Venda.class))).thenAnswer(inv -> inv.getArgument(0));

        VendaResponse response = vendaService.salvar(request);

        assertThat(response.getStatusPagamento()).isEqualTo(StatusPagamento.AGUARDANDO_PIX);
    }

    @Test
    void salvar_estoqueInsuficiente_deveLancarBusinessException() {
        produto.setQuantidadeEstoque(1);

        ItemVendaRequest item = new ItemVendaRequest();
        item.setProdutoId(1L);
        item.setQuantidade(5);

        VendaRequest request = new VendaRequest();
        request.setFormaPagamento(FormaPagamento.PIX);
        request.setItens(List.of(item));

        when(authService.usuarioLogado()).thenReturn(usuario);
        when(produtoRepository.findByIdAndEmpresa(1L, empresa)).thenReturn(Optional.of(produto));

        assertThatThrownBy(() -> vendaService.salvar(request))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Estoque insuficiente");
    }

    @Test
    void salvar_dinheiro_semValorRecebido_deveLancarBusinessException() {
        ItemVendaRequest item = new ItemVendaRequest();
        item.setProdutoId(1L);
        item.setQuantidade(1);

        VendaRequest request = new VendaRequest();
        request.setFormaPagamento(FormaPagamento.DINHEIRO);
        request.setValorRecebido(null);
        request.setItens(List.of(item));

        when(authService.usuarioLogado()).thenReturn(usuario);
        when(produtoRepository.findByIdAndEmpresa(1L, empresa)).thenReturn(Optional.of(produto));

        assertThatThrownBy(() -> vendaService.salvar(request))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("valor recebido");
    }

    @Test
    void salvar_dinheiro_valorInsuficiente_deveLancarBusinessException() {
        ItemVendaRequest item = new ItemVendaRequest();
        item.setProdutoId(1L);
        item.setQuantidade(2); // total = 5.00

        VendaRequest request = new VendaRequest();
        request.setFormaPagamento(FormaPagamento.DINHEIRO);
        request.setValorRecebido(new BigDecimal("4.00")); // menor que 5.00
        request.setItens(List.of(item));

        when(authService.usuarioLogado()).thenReturn(usuario);
        when(produtoRepository.findByIdAndEmpresa(1L, empresa)).thenReturn(Optional.of(produto));

        assertThatThrownBy(() -> vendaService.salvar(request))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Valor recebido não pode ser menor");
    }

    @Test
    void salvar_produtoNaoEncontrado_deveLancarResourceNotFoundException() {
        ItemVendaRequest item = new ItemVendaRequest();
        item.setProdutoId(999L);
        item.setQuantidade(1);

        VendaRequest request = new VendaRequest();
        request.setFormaPagamento(FormaPagamento.PIX);
        request.setItens(List.of(item));

        when(authService.usuarioLogado()).thenReturn(usuario);
        when(produtoRepository.findByIdAndEmpresa(999L, empresa)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> vendaService.salvar(request))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}
