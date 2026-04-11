package com.smartbusiness.repository;

import com.smartbusiness.entity.Empresa;
import com.smartbusiness.entity.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    List<Produto> findByEmpresa(Empresa empresa);
    Optional<Produto> findByIdAndEmpresa(Long id, Empresa empresa);
    long countByEmpresa(Empresa empresa);
    List<Produto> findByEmpresaAndNomeContainingIgnoreCase(Empresa empresa, String nome);
}
