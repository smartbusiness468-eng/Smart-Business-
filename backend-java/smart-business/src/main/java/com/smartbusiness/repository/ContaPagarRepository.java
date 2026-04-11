package com.smartbusiness.repository;

import com.smartbusiness.entity.ContaPagar;
import com.smartbusiness.entity.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ContaPagarRepository extends JpaRepository<ContaPagar, Long> {
    List<ContaPagar> findByEmpresaOrderByDataVencimentoAsc(Empresa empresa);
    Optional<ContaPagar> findByIdAndEmpresa(Long id, Empresa empresa);
}
