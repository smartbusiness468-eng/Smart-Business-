package com.smartbusiness.repository;

import com.smartbusiness.entity.ContaReceber;
import com.smartbusiness.entity.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ContaReceberRepository extends JpaRepository<ContaReceber, Long> {
    List<ContaReceber> findByEmpresaOrderByDataVencimentoAsc(Empresa empresa);
    Optional<ContaReceber> findByIdAndEmpresa(Long id, Empresa empresa);
}
