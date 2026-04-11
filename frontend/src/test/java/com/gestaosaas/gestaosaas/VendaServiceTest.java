package com.gestaosaas.gestaosaas;

import com.gestaosaas.gestaosaas.entity.*;
import com.gestaosaas.gestaosaas.repository.ClienteRepository;
import com.gestaosaas.gestaosaas.repository.ProdutoRepository;
import com.gestaosaas.gestaosaas.repository.VendaRepository;
import com.gestaosaas.gestaosaas.service.AuthService;
import com.gestaosaas.gestaosaas.service.VendaService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class VendaServiceTest {

    @Mock
    private VendaRepository vendaRepository;

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private ProdutoRepository produtoRepository;

    @Mock
    private AuthService authService;

    @InjectMocks
    private VendaService service;

    @Test
    void deveSalvarVendaDaEmpresaDoUsuarioLogado() {
        Empresa empresa = new Empresa();
        empresa.setNome("Empresa A");

        Usuario usuario = new Usuario();
        usuario.setEmpresa(empresa);

        Cliente cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNome("João");
        cliente.setEmpresa(empresa);

        Produto produto = new Produto();
        produto.setId(1L);
        produto.setNome("Teclado");
        produto.setPreco(new BigDecimal("100.00"));
        produto.setQuantidadeEstoque(10);
        produto.setEmpresa(empresa);

        ItemVenda itemForm = new ItemVenda();
        itemForm.setProduto(produto);
        itemForm.setQuantidade(2);

        Venda vendaForm = new Venda();
        vendaForm.setCliente(cliente);
        vendaForm.setFormaPagamento(FormaPagamento.CARTAO);
        vendaForm.setItens(java.util.List.of(itemForm));

        when(authService.usuarioLogado()).thenReturn(usuario);
        when(clienteRepository.findByIdAndEmpresa(1L, empresa)).thenReturn(Optional.of(cliente));
        when(produtoRepository.findByIdAndEmpresa(1L, empresa)).thenReturn(Optional.of(produto));
        when(vendaRepository.save(any(Venda.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Venda venda = service.salvarVendaCompleta(vendaForm);

        assertNotNull(venda);
        assertEquals(empresa, venda.getEmpresa());
        assertEquals(cliente, venda.getCliente());
        assertEquals(new BigDecimal("200.00"), venda.getValorTotal());
        assertEquals(8, produto.getQuantidadeEstoque());
        verify(produtoRepository, times(1)).save(produto);
        verify(vendaRepository, times(1)).save(any(Venda.class));
    }

    @Test
    void deveLancarErroQuandoProdutoNaoPertenceAEmpresa() {
        Empresa empresa = new Empresa();
        empresa.setNome("Empresa A");

        Usuario usuario = new Usuario();
        usuario.setEmpresa(empresa);

        Cliente cliente = new Cliente();
        cliente.setId(1L);
        cliente.setEmpresa(empresa);

        Produto produtoRef = new Produto();
        produtoRef.setId(1L);

        ItemVenda itemForm = new ItemVenda();
        itemForm.setProduto(produtoRef);
        itemForm.setQuantidade(1);

        Venda vendaForm = new Venda();
        vendaForm.setCliente(cliente);
        vendaForm.setFormaPagamento(FormaPagamento.CARTAO);
        vendaForm.setItens(java.util.List.of(itemForm));

        when(authService.usuarioLogado()).thenReturn(usuario);
        when(clienteRepository.findByIdAndEmpresa(1L, empresa)).thenReturn(Optional.of(cliente));
        when(produtoRepository.findByIdAndEmpresa(1L, empresa)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.salvarVendaCompleta(vendaForm));

        assertEquals("Produto não encontrado", ex.getMessage());
    }
}