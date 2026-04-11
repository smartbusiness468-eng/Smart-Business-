package com.smartbusiness.repository;

import com.smartbusiness.entity.Empresa;
import com.smartbusiness.entity.Venda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface VendaRepository extends JpaRepository<Venda, Long> {
    List<Venda> findByEmpresaOrderByDataVendaDesc(Empresa empresa);
    Optional<Venda> findByIdAndEmpresa(Long id, Empresa empresa);
    long countByEmpresa(Empresa empresa);

    @Query("SELECT COALESCE(SUM(v.valorTotal), 0) FROM Venda v WHERE v.empresa = :empresa AND v.dataVenda BETWEEN :inicio AND :fim")
    BigDecimal somaTotalVendasPorPeriodo(@Param("empresa") Empresa empresa,
                                         @Param("inicio") LocalDate inicio,
                                         @Param("fim") LocalDate fim);
}
