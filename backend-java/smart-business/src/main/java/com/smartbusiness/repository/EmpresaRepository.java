package com.smartbusiness.repository;

import com.smartbusiness.entity.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmpresaRepository extends JpaRepository<Empresa, Long> {
    Optional<Empresa> findBySlug(String slug);
    Optional<Empresa> findByCnpj(String cnpj);
}
