package com.smartbusiness.service;

import com.smartbusiness.dto.ProdutoDTO;
import com.smartbusiness.entity.Empresa;
import com.smartbusiness.entity.Produto;
import com.smartbusiness.exception.ResourceNotFoundException;
import com.smartbusiness.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final AuthService authService;

    public List<Produto> listarTodos() {
        Empresa empresa = authService.usuarioLogado().getEmpresa();
        return produtoRepository.findByEmpresa(empresa);
    }

    public List<Produto> buscarPorNome(String nome) {
        Empresa empresa = authService.usuarioLogado().getEmpresa();
        return produtoRepository.findByEmpresaAndNomeContainingIgnoreCase(empresa, nome);
    }

    public Produto buscarPorId(Long id) {
        Empresa empresa = authService.usuarioLogado().getEmpresa();
        return produtoRepository.findByIdAndEmpresa(id, empresa)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));
    }

    @Transactional
    public Produto salvar(ProdutoDTO dto) {
        Empresa empresa = authService.usuarioLogado().getEmpresa();

        Produto produto = new Produto();
        produto.setNome(dto.getNome());
        produto.setDescricao(dto.getDescricao());
        produto.setPreco(dto.getPreco());
        produto.setQuantidadeEstoque(dto.getQuantidadeEstoque());
        produto.setCodigoBarras(dto.getCodigoBarras());
        produto.setEmpresa(empresa);

        return produtoRepository.save(produto);
    }

    @Transactional
    public Produto atualizar(Long id, ProdutoDTO dto) {
        Produto produto = buscarPorId(id);
        produto.setNome(dto.getNome());
        produto.setDescricao(dto.getDescricao());
        produto.setPreco(dto.getPreco());
        produto.setQuantidadeEstoque(dto.getQuantidadeEstoque());
        produto.setCodigoBarras(dto.getCodigoBarras());
        return produtoRepository.save(produto);
    }

    @Transactional
    public void excluir(Long id) {
        Produto produto = buscarPorId(id);
        produtoRepository.delete(produto);
    }
}
