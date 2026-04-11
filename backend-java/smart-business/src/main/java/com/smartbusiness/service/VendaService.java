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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VendaService {

    private final VendaRepository vendaRepository;
    private final ClienteRepository clienteRepository;
    private final ProdutoRepository produtoRepository;
    private final AuthService authService;

    public List<VendaResponse> listarTodas() {
        Empresa empresa = authService.usuarioLogado().getEmpresa();
        return vendaRepository.findByEmpresaOrderByDataVendaDesc(empresa)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public VendaResponse buscarPorId(Long id) {
        return toResponse(buscarEntidade(id));
    }

    @Transactional
    public VendaResponse salvar(VendaRequest request) {
        Empresa empresa = authService.usuarioLogado().getEmpresa();

        Venda venda = new Venda();
        venda.setEmpresa(empresa);
        venda.setDataVenda(request.getDataVenda() != null ? request.getDataVenda() : LocalDate.now());
        venda.setObservacoes(request.getObservacoes());

        if (request.getClienteId() != null) {
            Cliente cliente = clienteRepository.findByIdAndEmpresa(request.getClienteId(), empresa)
                    .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));
            venda.setCliente(cliente);
        }

        BigDecimal total = processarItens(venda, request.getItens(), empresa);
        venda.setValorTotal(total);

        aplicarPagamento(venda, request, total);

        return toResponse(vendaRepository.save(venda));
    }

    @Transactional
    public VendaResponse atualizar(Long id, VendaRequest request) {
        Empresa empresa = authService.usuarioLogado().getEmpresa();
        Venda venda = vendaRepository.findByIdAndEmpresa(id, empresa)
                .orElseThrow(() -> new ResourceNotFoundException("Venda não encontrada"));

        devolverEstoque(venda);
        venda.getItens().clear();

        if (request.getDataVenda() != null) venda.setDataVenda(request.getDataVenda());
        venda.setObservacoes(request.getObservacoes());

        if (request.getClienteId() != null) {
            Cliente cliente = clienteRepository.findByIdAndEmpresa(request.getClienteId(), empresa)
                    .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));
            venda.setCliente(cliente);
        } else {
            venda.setCliente(null);
        }

        BigDecimal total = processarItens(venda, request.getItens(), empresa);
        venda.setValorTotal(total);
        aplicarPagamento(venda, request, total);

        return toResponse(vendaRepository.save(venda));
    }

    @Transactional
    public void excluir(Long id) {
        Venda venda = buscarEntidade(id);
        devolverEstoque(venda);
        vendaRepository.delete(venda);
    }

    private BigDecimal processarItens(Venda venda, List<ItemVendaRequest> itensRequest, Empresa empresa) {
        BigDecimal total = BigDecimal.ZERO;

        for (ItemVendaRequest req : itensRequest) {
            Produto produto = produtoRepository.findByIdAndEmpresa(req.getProdutoId(), empresa)
                    .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado: id " + req.getProdutoId()));

            if (produto.getQuantidadeEstoque() == null || produto.getQuantidadeEstoque() < req.getQuantidade()) {
                throw new BusinessException("Estoque insuficiente para o produto: " + produto.getNome());
            }

            BigDecimal precoUnitario = produto.getPreco().setScale(2, RoundingMode.HALF_UP);
            BigDecimal subtotal = precoUnitario.multiply(BigDecimal.valueOf(req.getQuantidade()))
                    .setScale(2, RoundingMode.HALF_UP);

            ItemVenda item = new ItemVenda();
            item.setProduto(produto);
            item.setQuantidade(req.getQuantidade());
            item.setPrecoUnitario(precoUnitario);
            item.setSubtotal(subtotal);
            venda.adicionarItem(item);

            produto.setQuantidadeEstoque(produto.getQuantidadeEstoque() - req.getQuantidade());
            produtoRepository.save(produto);

            total = total.add(subtotal);
        }

        return total.setScale(2, RoundingMode.HALF_UP);
    }

    private void aplicarPagamento(Venda venda, VendaRequest request, BigDecimal total) {
        venda.setFormaPagamento(request.getFormaPagamento());

        if (request.getFormaPagamento() == FormaPagamento.DINHEIRO) {
            BigDecimal valorRecebido = request.getValorRecebido();
            if (valorRecebido == null) {
                throw new BusinessException("Informe o valor recebido para pagamento em dinheiro");
            }
            valorRecebido = valorRecebido.setScale(2, RoundingMode.HALF_UP);
            if (valorRecebido.compareTo(total) < 0) {
                throw new BusinessException("Valor recebido não pode ser menor que o total da venda");
            }
            venda.setValorRecebido(valorRecebido);
            venda.setTroco(valorRecebido.subtract(total).setScale(2, RoundingMode.HALF_UP));
            venda.setStatusPagamento(StatusPagamento.PAGO);
        } else if (request.getFormaPagamento() == FormaPagamento.PIX) {
            venda.setStatusPagamento(StatusPagamento.AGUARDANDO_PIX);
        } else {
            venda.setTroco(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));
            venda.setStatusPagamento(StatusPagamento.PAGO);
        }
    }

    private void devolverEstoque(Venda venda) {
        for (ItemVenda item : venda.getItens()) {
            if (item.getProduto() != null && item.getQuantidade() != null) {
                Produto produto = item.getProduto();
                int estoqueAtual = produto.getQuantidadeEstoque() != null ? produto.getQuantidadeEstoque() : 0;
                produto.setQuantidadeEstoque(estoqueAtual + item.getQuantidade());
                produtoRepository.save(produto);
            }
        }
    }

    private Venda buscarEntidade(Long id) {
        Empresa empresa = authService.usuarioLogado().getEmpresa();
        return vendaRepository.findByIdAndEmpresa(id, empresa)
                .orElseThrow(() -> new ResourceNotFoundException("Venda não encontrada"));
    }

    private VendaResponse toResponse(Venda venda) {
        VendaResponse response = new VendaResponse();
        response.setId(venda.getId());
        response.setDataVenda(venda.getDataVenda());
        response.setValorTotal(venda.getValorTotal());
        response.setValorRecebido(venda.getValorRecebido());
        response.setTroco(venda.getTroco());
        response.setFormaPagamento(venda.getFormaPagamento());
        response.setStatusPagamento(venda.getStatusPagamento());
        response.setObservacoes(venda.getObservacoes());

        if (venda.getCliente() != null) {
            response.setClienteId(venda.getCliente().getId());
            response.setClienteNome(venda.getCliente().getNome());
        }

        List<VendaResponse.ItemVendaResponse> itens = venda.getItens().stream().map(item -> {
            VendaResponse.ItemVendaResponse ir = new VendaResponse.ItemVendaResponse();
            ir.setId(item.getId());
            ir.setQuantidade(item.getQuantidade());
            ir.setPrecoUnitario(item.getPrecoUnitario());
            ir.setSubtotal(item.getSubtotal());
            if (item.getProduto() != null) {
                ir.setProdutoId(item.getProduto().getId());
                ir.setProdutoNome(item.getProduto().getNome());
            }
            return ir;
        }).collect(Collectors.toList());

        response.setItens(itens);
        return response;
    }
}
