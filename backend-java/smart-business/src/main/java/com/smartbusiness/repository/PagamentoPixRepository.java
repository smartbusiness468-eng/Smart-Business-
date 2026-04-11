package com.smartbusiness.repository;

import com.smartbusiness.entity.PagamentoPix;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PagamentoPixRepository extends JpaRepository<PagamentoPix, Long> {
    Optional<PagamentoPix> findByVendaId(Long vendaId);
    Optional<PagamentoPix> findByTxid(String txid);
}
